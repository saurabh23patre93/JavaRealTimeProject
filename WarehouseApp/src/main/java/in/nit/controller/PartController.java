package in.nit.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import in.nit.model.Part;
import in.nit.model.ShipmentType;
import in.nit.service.IPartService;
import in.nit.service.IUomService;
import in.nit.util.PartUtil;
import in.nit.view.PartExcelView;
import in.nit.view.ShipmentTypePdfView;

@Controller
@RequestMapping("/part")
public class PartController {
	//Create Logger Object
	private Logger log=LoggerFactory.getLogger(PartController.class);	
	
	//a.HAS-A relation With IUomService
	@Autowired
	private IUomService uomService;
	
	//For PIE And BAR chart
	@Autowired
	private PartUtil util;
	
	@Autowired
	private ServletContext context;
	
	@Autowired
	private IPartService service;
	
	/**b.Get DropDown Map Data
	 *call this method inside other controller methods
	 *where return page is Register or edit 		
	**/
	private void addDropDownUI(Model model) {
		model.addAttribute("uoms",uomService.getUomIdAndModel());
	}
	
	//1.Show Register Page
	/**
	 * URL:/register
	 * Goto Page PartRegister.html
	 */
	@GetMapping("/register")
	public String showRegister(Model model) {
		//Form Backing Object
		model.addAttribute("part", new Part());
		addDropDownUI(model);
		return "PartRegister";
	}
	
	//2.save:on click submit
	/**
	 * URL: /save, Type: POST
	 * Goto : PartRegister
	 */
	@PostMapping("/save")
	public String save(//Read from Data from Ui
						@ModelAttribute Part part,
						Model model//to send data to UI
						) {
		
		String id=null;
		String message=null;
		
		log.info("Entered Into Part Saved method");
		try {
			//Perform Save operation
			id=service.savePart(part);
			//Construct one message
			message="Part "+id +" saved successfully";
			log.debug(message);
			//send message to UI
			model.addAttribute("message",message);
			log.info("Form backing for part model");
			model.addAttribute("part",new Part());
			addDropDownUI(model);
		} catch (Exception e) {
			log.error("Unable to save::"+e.getMessage());
			e.printStackTrace();
		}
		log.info("Back to UI page");
		//Goto UI page
		return "PartRegister";
	}
	
	//3.Show all data
	@GetMapping("/all")
	public String fetchAll(Model model) {
		List<Part> list=null;
		
		log.info("Entered Into fetchAll Part method");
		try {
			list=service.getAllParts();
			model.addAttribute("list",list);
			addDropDownUI(model);
			log.info("Got Data from DB using service:size of list is:"+list.size());
		} catch (Exception e) {
			log.error("Unable to fetch Data from DB:"+e.getMessage());
			e.printStackTrace();
		}
		log.info("Back to UI page For data Display");
		return "PartData";
	}
	
	//4.Delete data by id
	@GetMapping("/delete/{id}")
	public String removeById(@PathVariable String id,Model model) {
		String msg=null;
		List<Part> list=null;
		
		log.info("Entered into Delete Method to delete with id:"+id);
		try {
			if (service.isPartExist(id)) {
				service.deletePart(id);
				msg="Part "+id+" deleted";
				log.debug(msg);
			} else {
				msg="Part "+id+" Not exist";
				log.warn(msg);
			}
			model.addAttribute("message",msg);
			log.info("Fetching new Data after Delete");
			//Show other rows
			list=service.getAllParts();
			model.addAttribute("list",list);
		} catch (Exception e) {
			log.error("Unable to Perform Delete Operation::"+e.getMessage());
			e.printStackTrace();
		}
		log.info("Back to UI data display page");
		return "PartData";
	}
	/**
	 * On click Edit HyperLink at UI,
	 * read one PathVariable and fetch data from 
	 * service, if exist send to edit page
	 * else redirect to data page
	 */
	 //5. Show Edit Page with data
	@GetMapping("/edit/{id}")
	public String showEdit(@PathVariable String id,
							Model model) {
		String page=null;
		Optional<Part> opt=null;
		Part part=null;
		
		log.info("Entered into EDit operation with id:"+id);
		try {
			
			opt=service.getOnePart(id);
			log.info("Service called is Made");
			if (opt.isPresent()) {
				log.info("Data exist with id:"+id);
				part=opt.get();
				//Form backing object with data
				model.addAttribute("part",part);
				addDropDownUI(model);
				page="PartEdit";
			} else {
				log.warn("Data not exist with id:"+id);
				//Id is not exist
				page="redirect:../all";
			}
		} catch (Exception e) {
			log.error("Unable to Perform Edit Operation:"+e.getMessage());
			e.printStackTrace();
		}
		log.info("Back to UI page");
		return page;
	}
	
	/**
	 * On click update button,read form data and perform update operation
	 * send back to Data page with success message.
	 */
	//6. Update: on click update
	@PostMapping("/update")
	public String update(@ModelAttribute Part Part,
						Model model) {
		String msg=null;
		List<Part> list=null;
		
		log.info("Enterd into Update operaion ");
		try {
			service.updatePart(Part);
			msg="Part "+Part.getId() +" Updated";
			model.addAttribute("message",msg);
			log.debug(msg);
			
			log.info("Fetching latest Data");
			//new data from DB
			list=service.getAllParts();
			model.addAttribute("list",list);
		} catch (Exception e) {
			log.error("Unable to update data"+e.getMessage());
			e.printStackTrace();
		}
		log.info("Back to UI page");
		return "PartData";
	}
	/**
	 * Method that gets data from db table
	 * and returns ModelAndView having ViewClass object and 
	 * data as list
	 */
	//7.Export Data to Excel file
	@GetMapping("/excel")
	public ModelAndView exportToExcel() {
		ModelAndView mav=null;
		List<Part> list=null;
		
		try {
			log.info("Enterd into Export Data to excel file method");
			mav=new ModelAndView();
			mav.setView(new PartExcelView());
			
			//Send data to View Class
			log.info("Send data to view class");
			list=service.getAllParts();
			mav.addObject("obs",list);
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
		log.info("return Model and view object");
		return mav;
	}
	
	/**
	 * To gets one Row data based on ID given and 
	 * sent to ViewClass as list format bcoz UI for 
	 * Excel is desgined
	 */
	//8.Export One row to Excel File
	@GetMapping("/excel/{id}")
	public ModelAndView exportToExcelOne(@PathVariable String id) {
		
		ModelAndView mav=null;
		Optional<Part> opt=null;
		
		try {
			log.info("Enter into excel with id"+id);
			mav=new ModelAndView();
			mav.setView(new PartExcelView());
			opt=service.getOnePart(id);
			if (opt.isPresent()) {
				mav.addObject("obs", Arrays.asList(opt.get()));
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
		log.info("Send data to view class one object as list");
		//Send data to view class one object as list
		return mav;
	}
	
	//9.Export all row to pdf file
		@GetMapping("/pdf")
		public ModelAndView exportPdfAll() {
			ModelAndView mav=null;
			List<Part> list=null;
			
			try {
				log.info("Enterd into Export Data to PDF file method");
				mav=new ModelAndView();
				mav.setView(new ShipmentTypePdfView());
				
				//get data from service
				log.info("Send data to view class");
				list=service.getAllParts();
				mav.addObject("list",list);
			} catch (Exception e) {
				log.error(e.getMessage());
				e.printStackTrace();
			}
			log.info("return Model and view object");
			return mav;
		}
		
		//10.Export One row to Pdf
		@GetMapping("/pdf/{id}")
		public ModelAndView exportPdfOne(@PathVariable String id) {
			ModelAndView mov=null;
			Optional<Part> opt=null;
			
			try {
				log.info("Enter into PDF with id"+id);
				mov=new ModelAndView();
				mov.setView(new ShipmentTypePdfView());
				
				//Get data from service
				log.info("Get One  Data from service");
				opt=service.getOnePart(id);
				if (opt.isPresent()) {
					mov.addObject("list",Arrays.asList(opt.get()));
				}
			} catch (Exception e) {
				log.error(e.getMessage());
				e.printStackTrace();
			}
			log.info("Send data to view class one object as list");
			//Send data to view class one object as list
			return mov;
		}
		
		//11.For Generating PIE and BAR charts
		@GetMapping("/charts")
		public String generateCharts() {
			List<Object[]> list=null;
			String location=null;
			
			try {
				log.info("Enter Into generate Chart and show data");
				//Data toShow at chart
				list=service.getPartCodeCount();
				
				//Dynamic Temp folder location for service instance
				location=context.getRealPath("/");
				log.info("CHART LOCATION IS: " + location );
				
				//call chart methods
				util.generatePieChart(location,list);
				util.generateBarChart(location,list);
			} catch (Exception e) {
				log.error("Enable to OPen charts " +e.getMessage());
				e.printStackTrace();
			}
			
			log.info("Return to UI page Of Charts");
			return "PartCharts";
		}
		
		//----------------------------------------------Additional Client Side Form validation Based on Shipment Code----------------------------
		//---AJAX VALIDATION----------
			//.. /part/validatecode?code=ABCD
			@GetMapping("/validatepartcode")////In the rest controller, set default value to the request param as @Requestparam(required=false, defaultValue = “Unknown”). The default value is assigned if the request parameter is not available in the url.
			public @ResponseBody String validatePartCode(@RequestParam String partCode
														) 
			{
				String message="";
				
				try {
					log.info("Enter into the Part code validation"+partCode);
					if(service.isPartCodeExist(partCode)) {
						message = "Part Code <b>'"+partCode+"' Already exist</b>!";
						log.debug(message);
					}
				} catch (Exception e) {
					log.error("Unable to validate code"+e.getMessage());
					e.printStackTrace();
				}
				log.info("Return the message to UI page"+message);
				return message;
			}
	}
