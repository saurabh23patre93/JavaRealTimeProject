package in.nit.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import in.nit.service.IShipmentTypeService;
import in.nit.util.ShipmentTypeUtil;
import in.nit.view.ShipmentTypeExcelView;
import in.nit.view.ShipmentTypePdfView;

@Controller
@RequestMapping("/shipmenttype")
public class ShipmentTypeController {
	//Create Logger Object
	private Logger log=LoggerFactory.getLogger(ShipmentTypeController.class);;	
	
	//HAS-A relation with ServletContext and ShipmentTypeUtil
	@Autowired
	private ServletContext context;
	
	@Autowired
	private ShipmentTypeUtil util;
	
	@Autowired
	private IShipmentTypeService service;
	
	//1.Show Register Page
	/**
	 * URL:/register
	 * Goto Page ShipmentTypeRegister.html
	 */
	@GetMapping("/register")
	public String showRegister(Model model) {
		//Form Backing Object
		model.addAttribute("shipmentType", new ShipmentType());
		return "ShipmentTypeRegister";
	}
	
	//2.save:on click submit
	/**
	 * URL: /save, Type: POST
	 * Goto : ShipmentTypeRegister
	 */
	@PostMapping("/save")
	public String save(//Read from Data from Ui
						@ModelAttribute ShipmentType shipmentType,
						Model model//to send data to UI
						) {
		
		Integer id=null;
		String message=null;
		
		log.info("Entered Into ShipmentSaved method");
		try {
			log.info("operation to save data into db::"+shipmentType);
			//Perform Save operation
			id=service.saveShipmentType(shipmentType);
			log.info("operation to save data into db::"+shipmentType+id);
			//Construct one message
			message="ShipmentType "+id +" saved successfully";
			log.debug(message);
			//send message to UI
			model.addAttribute("message",message);
			//Form backing object
			model.addAttribute("shipmentType",new ShipmentType());
		} catch (Exception e) {
			log.error("Unable to save::"+e.getMessage());
			e.printStackTrace();
		}
		log.info("Back to UI page");
		//Goto UI page
		return "ShipmentTypeRegister";
	}
	
	//3.Show all data
	@GetMapping("/all")
	public String fetchAll(@PageableDefault(page = 0,size = 3) Pageable pageable,
							Model model) {
		Page<ShipmentType> page=null;
		
		log.info("Enter Into fetchAll shipmentType method");
		try {
			page=service.getAllShipmentTypes(pageable);
			model.addAttribute("page",page);
			log.info("Got Data from DB using service:size of list is:"+page.getSize());
		} catch (Exception e) {
			log.error("Unable to fetch Data from DB:"+e.getMessage());
			e.printStackTrace();
		}
		log.info("Back to UI page For data Display");
		return "ShipmentTypeData";
	}
	
	//4.Delete data by id
	@GetMapping("/delete/{id}")
	public String removeById(@PathVariable Integer id,
							Model model,
							@PageableDefault(page = 0,size = 3) Pageable pageable) {
		String msg=null;
		Page<ShipmentType> page=null;
		
		log.info("Entered into Delete Method to delete with id:"+id);
		try {
			if (service.isShipmentTypeExist(id)) {
				service.deleteShipmentType(id);
				msg="ShipmentType "+id+" deleted";
				log.debug(msg);
			} else {
				msg="ShipmentType "+id+" Not exist";
				log.warn(msg);
			}
			model.addAttribute("message",msg);
			log.info("Fetching new Data after Delete");
			//Show other rows
			page=service.getAllShipmentTypes(pageable);
			model.addAttribute("page",page);
		} catch (Exception e) {
			log.error("Unable to Perform Delete Operation::"+e.getMessage());
			e.printStackTrace();
		}
		log.info("Back to UI data display page");
		return "ShipmentTypeData";
	}
	/**
	 * On click Edit HyperLink at UI,
	 * read one PathVariable and fetch data from 
	 * service, if exist send to edit page
	 * else redirect to data page
	 */
	 //5. Show Edit Page with data
	@GetMapping("/edit/{id}")
	public String showEdit(@PathVariable Integer id,
							Model model) {
		String page=null;
		Optional<ShipmentType> opt=null;
		ShipmentType st=null;
		
		log.info("Entered into EDit operation with id:"+id);
		try {
			
			opt=service.getOneShipmentType(id);
			log.info("Service called is Made");
			if (opt.isPresent()) {
				log.info("Data exist with id:"+id);
				st=opt.get();
				//Form backing object with data
				model.addAttribute("shipmentType",st);
				page="ShipmentTypeEdit";
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
	public String update(@ModelAttribute ShipmentType shipmentType,
						@PageableDefault(page = 0,size = 3) Pageable pageable,
						Model model) {
		String msg=null;
		Page<ShipmentType> page=null;
		
		log.info("Enterd into Update operaion ");
		try {
			service.updateShipmentType(shipmentType);
			msg="ShipmentType "+shipmentType.getId() +" Updated";
			model.addAttribute("message",msg);
			log.debug(msg);
			
			
			//new data from DB
			page=service.getAllShipmentTypes(pageable);
			log.info("Fetching latest Data");
			model.addAttribute("page",page);
		} catch (Exception e) {
			log.error("Unable to update data"+e.getMessage());
			e.printStackTrace();
		}
		log.info("Back to UI page");
		return "ShipmentTypeData";
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
		List<ShipmentType> list=null;
		
		try {
			log.info("Enterd into Export Data to excel file method");
			mav=new ModelAndView();
			mav.setView(new ShipmentTypeExcelView());
			
			//Send data to View Class
			log.info("Send data to view class");
			list=service.getAllShipmentTypes();
			mav.addObject("obs",list);
		} catch (Exception e) {
			log.error("Unable to Export "+e.getMessage());
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
	public ModelAndView exportToExcelOne(@PathVariable Integer id) {
		
		ModelAndView mav=null;
		Optional<ShipmentType> opt=null;
		
		try {
			log.info("Enter into excel with id"+id);
			mav=new ModelAndView();
			mav.setView(new ShipmentTypeExcelView());
			opt=service.getOneShipmentType(id);
			if (opt.isPresent()) {
				mav.addObject("obs", Arrays.asList(opt.get()));
			}
		} catch (Exception e) {
			log.error("Unable to Export "+e.getMessage());
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
		List<ShipmentType> list=null;
		
		try {
			log.info("Enterd into Export Data to PDF file method");
			mav=new ModelAndView();
			mav.setView(new ShipmentTypePdfView());
			
			//get data from service
			log.info("Send data to view class");
			list=service.getAllShipmentTypes();
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
	public ModelAndView exportPdfOne(@PathVariable Integer id) {
		ModelAndView mov=null;
		Optional<ShipmentType> opt=null;
		
		try {
			log.info("Enter into PDF with id"+id);
			mov=new ModelAndView();
			mov.setView(new ShipmentTypePdfView());
			
			//Get data from service
			log.info("Get One Shipment Data from service");
			opt=service.getOneShipmentType(id);
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
	
	//11.For Generate Char pie 
	@GetMapping("/charts")
	public String generateCharts() {
		List<Object[]> list=null;
		String location=null;
		
		try {
			log.info("Enter Into generate Chart and show data");
			//Data toShow at chart
			list=service.getShipmentModeCount();
			
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
		return "ShipmentTypeCharts";
	}
		
	//----------------------------------------------Additional Client Side Form validation Based on Shipment Code----------------------------
	//---AJAX VALIDATION----------
		//.. /shipmenttype/validatecode?code=ABCD
		@GetMapping("/validatecode")
		public @ResponseBody String validateShipmentCode(@RequestParam String code) 
		{
			String message="";
			
			try {
				log.info("Enter into the shipment code validation"+code);
				if(service.isShipmentTypeCodeExist(code)) {
					message = "Shipment Code <b>'"+code+"' Already exist</b>!";
					log.debug(message);
				}
			} catch (Exception e) {
				log.error("Unable to validate code"+e.getMessage());
				e.printStackTrace();
			}
			log.info("Return the message to UI page"+message);
			return message;
		}
		
		//For Google Charts
		@GetMapping("/gcharts")
		public @ResponseBody List<Object[]> getGoogleChartsData(){
			List<Object[]> list=null;
			
			//try {
				log.info("Enter to the GetGoogleChartsData ");
				//sample OUtput [{'Air',10},{'Train',20}]
				list=service.getShipmentModeCount();
		//	} catch (Exception e) {
		//		log.error("Enable to view Google charts "+e.getMessage());
		//		e.printStackTrace();
		//	}	
			log.info("Go Back to chart page");
			return list;
		}
}
