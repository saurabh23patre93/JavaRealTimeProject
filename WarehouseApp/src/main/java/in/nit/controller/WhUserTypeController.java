package in.nit.controller;


import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.servlet.ServletContext;

import org.hibernate.validator.constraints.Range;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import in.nit.model.ShipmentType;
import in.nit.model.WhUserType;
import in.nit.service.IWhUserTypeService;
import in.nit.util.EmailUtil;
import in.nit.util.WhUserTypeUtil;
import in.nit.view.ShipmentTypeExcelView;
import in.nit.view.ShipmentTypePdfView;
import in.nit.view.WhUserTypeExcelView;

@Controller
@RequestMapping("/whusertype")
public class WhUserTypeController {
	
	//Create Logger Object
	private Logger log=LoggerFactory.getLogger(WhUserTypeController.class);
	
	//HAS-A relation with ServletContext and WhUserTypeUtil
	@Autowired
	private IWhUserTypeService service;
	@Autowired
	private ServletContext context;
	@Autowired
	private WhUserTypeUtil util;
	@Autowired
	private EmailUtil emailUtil;
	
	//1.Show Register Page
	/**
	 * URL:/register
	 * Goto Page WhUserTypeRegister.html
	 */
	@GetMapping("/register")
	public String showRegister(Model model) {
		model.addAttribute("whUserType",new WhUserType());
		return "WhUserTypeRegister";
	}
	
	//2.save:on click submit
	/**
	 * URL: /save, Type: POST
	 * Goto : WhUserTypeRegister
	 */
	@PostMapping("/save")
	public String save(//Read from Data from UI
						@ModelAttribute WhUserType whUserType,
						Model model,//to send data to UI
						@RequestParam("fileOb") MultipartFile fileOb
						) {
		
		Integer id=null;
		String message=null;
		boolean flag=false;
		
		log.info("Enter into WhUserType save Method");
		try {
			log.info("Operation to save data into DB ::"+whUserType);
			//Perform Save operation
			id=service.saveWhUserType(whUserType);
			log.info("Operation to save data into DB with ID ::"+whUserType+" "+id);
			//Construct one message
			message="WhUserType "+id +" saved successfully";
			log.debug(message);
			//Send email to save
			flag=emailUtil.send(
					whUserType.getUserEmail(), 
					"WELCOME", 
					"Hello User:"+whUserType.getUserCode() 
					+", You are type:"+whUserType.getUserIdType(),
					fileOb);
			System.out.println(flag);
			
			if (flag) {
				message+=", Email also sent!";
			} else {
				message+=", Email is not sent!";
			}
			
//			if (id!=null) {
//				new Thread(new Runnable() {
//					public void run() {
//						boolean flag=emailUtil.send(whUserType.getUserEmail(),"WELCOME",
//									"Hello User: "+whUserType.getUserCode() 
//									+", You  are type: "+whUserType.getUserIdType());	
//						System.out.println(flag);
//					}
//				}).start();
				//if(flag) message+=", Email also sent!";
				//else message+=", Email is not sent!";
			//}
			
			//send message to UI
			model.addAttribute("message",message);
			
			//Form backing object
			model.addAttribute("whUserType",new WhUserType());
		} catch (Exception e) {
			log.error("Unable to Save:: "+e.getMessage());
			e.printStackTrace();
		}
		
		log.info("Back to UI page");
		//Goto page
		return "WhUserTypeRegister";
	}
	
	//3.Show all data
	@GetMapping("/all")
	public String fetchAll(Model model) {
		List<WhUserType> list=null;
		
		log.info("Enter into fetchAll WhUserType mehtod");
		try {
			list=service.getAllWhUserTypes();
			model.addAttribute("list", list);
			log.info("Got Data from DB using service :size of list is :"+list.size());
		} catch (Exception e) {
			log.error("Unable to fetch Data from DB::"+e.getMessage());
			e.printStackTrace();
		}
		
		log.info("Back to UI page For display data");
		return "WhUserTypeData";
	}
	
	//4.Delete Data using by id
	@GetMapping("/delete/{id}")
	public String removeById(@PathVariable Integer id,Model model) {
		String msg=null;
		List<WhUserType> list=null;
		
		log.info("Enter into Delete method to delete with id:"+id);
		try {
			if (service.isWhUserTypeExist(id)) {
				service.deleteWhUserType(id);
				msg="WhUserType "+id+" is deleted";
				log.debug(msg);
			} else {
				msg="WhUserType "+id+" is not exist.";
				log.warn(msg);
			}
			model.addAttribute("message",msg);
			log.info("Fetching new Data after delete with id");
			//show all data
			list=service.getAllWhUserTypes();
			model.addAttribute("list",list);
		} catch (Exception e) {
			log.error("Unable to Delete data with id "+e.getMessage());
			e.printStackTrace();
		}
		
		log.info("Back to UI page");
		return "WhUserTypeData";
	}
	
	/**
	 * On click Edit HyperLink at UI,
	 * read one PathVariable and fetch data from 
	 * service, if exist send to edit page
	 * else redirect to data page
	 */
	 //5. Show Edit Page with data
	@GetMapping("/edit/{id}")
	public String showEdit(@PathVariable Integer id,Model model) {
		String page=null;
		Optional<WhUserType> opt=null;
		WhUserType whUserType=null;
		
		log.info("Enter into Edit operation with id "+id);
		try {
			opt=service.getOneWhUserType(id);
			log.info("Service called to get WhUserType with id");
			if (opt.isPresent()) {
				log.info("Data exist with id "+id);
				whUserType=opt.get();
				//Form Backing Object with Data
				model.addAttribute("whUserType",whUserType);
				log.info("Back to Edit page");
				page="WhUserTypeEdit";
			} else {
				log.warn("Data not Exist with id "+id);
				//id not exist
				page="redirect:../all";
			}
		} catch (Exception e) {
			log.error("Unable to Edit "+e.getMessage());
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
	public String update(@ModelAttribute WhUserType whUserType,
						Model model) {
		String msg=null;
		List<WhUserType> list=null;
		
		log.info("Enter into Update Operation");
		try {
			service.updateWhUserType(whUserType);
			msg="WhUserType "+whUserType.getId()+" Updated";
			model.addAttribute("message",msg);
			log.debug(msg);
			
			
			//new data from db
			list=service.getAllWhUserTypes();
			log.info("Fetching latest Data");
			model.addAttribute("list",list);
		} catch (Exception e) {
			log.error("Unable to Update "+e.getMessage());
			e.printStackTrace();
		}
		
		log.info("Back to UI page");
		return "WhUserTypeData";
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
		List<WhUserType> list=null;
		
		try {
			log.info("Enterd into Export Data to excel file method");
			mav=new ModelAndView();
			mav.setView(new WhUserTypeExcelView());
			
			//Send data to View Class
			log.info("Send data to view class");
			list=service.getAllWhUserTypes();
			mav.addObject("obs",list);
		} catch (Exception e) {
			log.error("Unable to export excel "+e.getMessage());
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
		Optional<WhUserType> opt=null;
		
		try {
			log.info("Enter into excel with id"+id);
			mav=new ModelAndView();
			mav.setView(new WhUserTypeExcelView());
			opt=service.getOneWhUserType(id);
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
			List<WhUserType> list=null;
			
			try {
				log.info("Enterd into Export Data to PDF file method");
				mav=new ModelAndView();
				mav.setView(new ShipmentTypePdfView());
				
				//get data from service
				log.info("Send data to view class");
				list=service.getAllWhUserTypes();
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
		public ModelAndView exportPdfOne(@PathVariable Integer id) {
			ModelAndView mov=null;
			Optional<WhUserType> opt=null;
			
			try {
				log.info("Enter into PDF with id"+id);
				mov=new ModelAndView();
				mov.setView(new ShipmentTypePdfView());
				
				//Get data from service
				log.info("Get One  Data from service");
				opt=service.getOneWhUserType(id);
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
				//list=service.getShipmentModeCount();
				
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
			return "WhUserTypeCharts";
		}
		
		//-------------------------AJAX Mail Validation-----------------------------------------------------
		@GetMapping("/mailcheck")
		public @ResponseBody String validateEmail(@RequestParam String mail) {
			String message="";
			
			if (service.isWhUserTypeEmailExist(mail)) {
				message=mail+"  <b>already exist!</b>";
			}
			return message;
		}
		
		//---AJAX VALIDATION----------
				//.. /whusertype/validatecode?code=ABCD
				@GetMapping("/validateusercode")
				public @ResponseBody String validateWhUserTypeCode(@RequestParam String code) 
				{
					String message="";
					
					try {
						log.info("Enter into the code WhUserType validation "+code);
						if(service.isWhUserTypeCodeExist(code)) {
							System.out.println("validation userCode");
							message = "WhUserType Code <b>'"+code+"' Already exist</b>!";
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
