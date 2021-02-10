package in.nit.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.servlet.ServletContext;

import org.hibernate.annotations.GenericGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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

import in.nit.model.ShipmentType;
import in.nit.model.Uom;
import in.nit.service.IUomService;
import in.nit.util.UomUtil;
import in.nit.view.ShipmentTypeExcelView;
import in.nit.view.ShipmentTypePdfView;
import in.nit.view.UomExcelView;
import in.nit.view.UomPdfView;

@Controller
@RequestMapping("/uom")
public class UomController {
	//Create Logger Object
	private Logger log=LoggerFactory.getLogger(UomController.class);
	
	@Autowired
	private IUomService service;
	
	@Autowired
	private UomUtil util;
	
	@Autowired
	private ServletContext context;
	
	//1.Show Register Page
	/**
	 * URL:/register
	 * Goto Page UomRegister.html
	 */
	@GetMapping("/register")
	public String showRegister(Model model) {
		model.addAttribute("uom", new Uom());
		return "UomRegister";
	}
	
	//2.save:on click submit
	/**
	 * URL: /save, Type: POST
	 * Goto : UomRegister
	 */
	@PostMapping("/save")
	public String save(//Read from Data from Ui
						@ModelAttribute Uom uom,
						Model model//to send data to UI
						) {
		
		String id=null;
		String message=null;
		log.info("Entered into UOMSaved Method");
		try {
			log.info("Operation to save data into DB::"+uom);
			//Perform Save operation
			id=service.saveUom(uom);
			log.info("Operation to save data into DB::"+uom+id);
			//Construct one message
			message="Uom "+id +" saved successfully";
			log.debug(message);
			//send message to UI
			model.addAttribute("message",message);
			//Form backing object
			model.addAttribute("uom",new Uom());
		} catch (Exception e) {
			log.error("Unable to save::"+e.getMessage());
			e.printStackTrace();
		}
		log.info("Back to UI page");
		//Goto page
		return "UomRegister";
	}
	
	//3.Show all data
	@GetMapping("/all")//For pagination purpose
	public String fetchAll(@PageableDefault(page = 0,size = 3) Pageable pageable,
							Model model) {
		Page<Uom> page=null;
		
		log.info("Enter into fetchAll UOM method");
		try {
			page=service.getAllUoms(pageable);
			model.addAttribute("page", page);
			log.info("Got data from DB using service :size of list is:"+page);
		} catch (Exception e) {
			log.error("Unable to fetch Data form DB::"+e.getMessage());
			e.printStackTrace();
		}
		log.info("Back to UI page for Data display");
		return "UomData";
	}
	
	//4.Delete Data using by id
	@GetMapping("/delete/{id}")
	public String removeById(@PathVariable String id,
							@PageableDefault(page = 0,size = 3)Pageable pageable,
							Model model) {
		String msg=null;
		Page<Uom> page=null;
		
		log.info("Enter into delete method to delete with id"+id);
		try {
			if (service.isUomExist(id)) {
				try {
					service.deleteUom(id);
					msg="UOM "+id+" is deleted";
					log.debug(msg);
				} catch (DataIntegrityViolationException dive) {
					msg = "Uom '" + id + "' can not be deleted! It is used by Part";
					log.error("Unable to delete bcoz it used by Part model "+dive.getMessage());
					dive.printStackTrace();
				}
			} else {
				msg="UOM "+id+" is not exist.";
				log.warn(msg);
			}
			model.addAttribute("message",msg);
			log.info("Fetching new Data afete Delete");
			//show all data
			page=service.getAllUoms(pageable);
			model.addAttribute("page",page);
		} catch (Exception e) {
			log.error("Unable to perform Delete Operation::"+e.getMessage());
			e.printStackTrace();
		}
		
		log.info("Back to UI data display page");
		return "UomData";
	}
	
	/**
	 * On click Edit HyperLink at UI,
	 * read one PathVariable and fetch data from 
	 * service, if exist send to edit page
	 * else redirect to data page
	 */
	 //5. Show Edit Page with data
	@GetMapping("/edit/{id}")
	public String showEdit(@PathVariable String id,Model model) {
		String page=null;
		Optional<Uom> opt=null;
		Uom uom=null;
		
		log.info("Enter into Edit operation with id::"+id);
		try {
			opt=service.getOneUom(id);
			log.info("Service called is made");
			if (opt.isPresent()) {
				log.info("Data exist with id::"+id);
				uom=opt.get();
				page="UomEdit";
				//Form Backing Object with Data
				model.addAttribute("uom",uom);
				
			} else {
				log.warn("Data not exist with id::"+id);
				//id not exist
				page="redirect:../all";
			}
		} catch (Exception e) {
			log.error("Uable to Perform Edit operation::"+e.getMessage());
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
	public String update(@ModelAttribute Uom uom,
						@PageableDefault(page = 0,size = 3)Pageable pageable,
						Model model) {
		String msg=null;
		Page<Uom> page=null;
		
		log.info("Enter into Update operation method");
		try {
			service.updateUom(uom);
			msg="Uom "+uom.getId()+" Updated";
			model.addAttribute("message",msg);
			log.debug(msg);
			
			log.info("Fetching latest data");
			//new data from db
			page=service.getAllUoms(pageable);
			model.addAttribute("page",page);
		} catch (Exception e) {
			log.error("Unable to update data"+e.getMessage());
			e.printStackTrace();
		}
		
		log.info("Back to UI page UOMData");
		return "UomData";
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
		List<Uom> list=null;
		
		try {
			log.info("Enterd into Export Data to excel file method");
			mav=new ModelAndView();
			mav.setView(new UomExcelView());
			
			//Send data to View Class
			log.info("Send data to view class");
			list=service.getAllUoms();
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
		Optional<Uom> opt=null;
		
		try {
			log.info("Enter into excel with id"+id);
			mav=new ModelAndView();
			mav.setView(new UomExcelView());
			opt=service.getOneUom(id);
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
			List<Uom> list=null;
			
			try {
				log.info("Enterd into Export Data to PDF file method");
				mav=new ModelAndView();
				mav.setView(new UomPdfView());
				
				//get data from service
				log.info("Send data to view class");
				list=service.getAllUoms();
				mav.addObject("list",list);
			} catch (Exception e) {
				log.error("Unable to pdf"+e.getMessage());
				e.printStackTrace();
			}
			log.info("return Model and view object");
			return mav;
		}
		
		//10.Export One row to Pdf
		@GetMapping("/pdf/{id}")
		public ModelAndView exportPdfOne(@PathVariable String id) {
			ModelAndView mov=null;
			Optional<Uom> opt=null;
			
			try {
				log.info("Enter into PDF with id"+id);
				mov=new ModelAndView();
				mov.setView(new UomPdfView());
				
				//Get data from service
				log.info("Get One  Data from service");
				opt=service.getOneUom(id);
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
		
		//11.For Generating PIE and BAR chars
		@GetMapping("/charts")
		public String generateCharts() {
			List<Object[]> list=null;
			String location=null;
			
			try {
				log.info("Enter Into generate Chart and show data");
				//Data toShow at chart
				list=service.getUomModelCount();
				
				//Dynamic Temp folder location for service instance
				location=context.getRealPath("/");
				log.info("CHART LOCATION IS: " + location );
				
				//call chart methods
				util.generatePieChart(location,list);
				util.generateBarChart(location,list);
			} catch (Exception e) {
				log.error(e.getMessage());
				e.printStackTrace();
			}
			log.info("Return to UI page");
			return "UomCharts";
		}
		
		//For Google Charts
				@GetMapping("/gcharts")
				public @ResponseBody List<Object[]> getGoogleChartsData(){
					List<Object[]> list=null;
					
				//	try {
						log.info("Enter to the GetGoogleChartsData ");
						//sample OUtput [{'Air',10},{'Train',20}]
						list=service.getUomModelCount();
				//	} catch (Exception e) {
				//		log.error("Enable to view Google charts "+e.getMessage());
				//		e.printStackTrace();
				//	}	
					log.info("Go Back to chart page");
					return list;
				}
		//=============================================Additonal client side uomModel validation===============================
		//AJAX validation
		//---/uom/validatemodel?model=?
		@GetMapping("/validatemodel")//@RequestParam(name = "model",required = true,defaultValue = "unknown")
		public @ResponseBody String validateUomModel(@RequestParam//(value = "model",required = false) 
													String uomModel) {
			String message="";
			
			try {
				log.info("Enter into the Uom model validation "+uomModel);
				if (service.isUomModelExist(uomModel)) {
					message="Uom Model <b> "+uomModel+"' Already exist</b>!";
					log.debug(message);
				}
			} catch (Exception e) {
				log.error("Unable to validate Model "+ e.getMessage());
				e.printStackTrace();
			}
			log.info("Return the message to UI page "+message);
			return message;
		}
}
