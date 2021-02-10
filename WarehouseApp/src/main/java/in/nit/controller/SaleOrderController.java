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

import in.nit.model.SaleDetail;
import in.nit.model.SaleOrder;
import in.nit.service.IPartService;
import in.nit.service.ISaleOrderService;
import in.nit.service.IShipmentTypeService;
import in.nit.service.IWhUserTypeService;
import in.nit.util.SaleOrderUtil;
import in.nit.view.CustomerInvoicePdfView;
import in.nit.view.SaleOrderExcelView;
import in.nit.view.SaleOrderPdfView;

@Controller
@RequestMapping("/saleorder")
public class SaleOrderController {
	//Create Logger Object
	private Logger log=LoggerFactory.getLogger(SaleOrderController.class);	
	
	@Autowired
	private ISaleOrderService service;
	
	//a.HAS-A relation With IShipmentTypeService
	@Autowired
	private IShipmentTypeService shipmentTypeService;
	//a.HAS-A relation With IWhUserTypeService
	@Autowired
	private IWhUserTypeService whUserTypeService;
	
	//For PIE And BAR chart
	@Autowired
	private SaleOrderUtil util;
	
	@Autowired
	private ServletContext context;
	
	@Autowired
	private IPartService partService;
	
	/**b.Get DropDown Map Data to achive inbound operation
	 *call this method inside other controller methods
	 *where return page is Register or edit 		
	**/
	private void addDorpDownUiForDtls(Model model) {
		model.addAttribute("items", partService.getPartIdAndCode());
	}	
	/**b.Get DropDown Map Data
	 *call this method inside other controller methods
	 *where return page is Register or edit 		
	**/
	private void addDropDownUI(Model model) {
		model.addAttribute("shipmentTypes",shipmentTypeService.getShipmentTypeIdAndModel());
		model.addAttribute("whUserTypes",whUserTypeService.getWhUserTypeIdAndCode("Customer"));
	}
	
	//1.Show Register Page
	/**
	 * URL:/register
	 * Goto Page ParchaseOrderRegister.html
	 */
	@GetMapping("/register")
	public String showRegister(Model model) {
		//Form Backing Object
		model.addAttribute("saleOrder", new SaleOrder());
		addDropDownUI(model);
		return "SaleOrderRegister";
	}
	
	//2.save:on click submit
	/**
	 * URL: /save, Type: POST
	 * Goto : SaleOrderRegister
	 */
	@PostMapping("/save")
	public String save(//Read from Data from Ui
						@ModelAttribute SaleOrder saleOrder,
						Model model//to send data to UI
						) {
		
		Integer id=null;
		String message=null;
		
		log.info("Entered Into  Saved method");
		try {
			//Perform Save operation
			id=service.saveSaleOrder(saleOrder);
			//Construct one message
			message="Sale Order "+id +" saved successfully";
			log.debug(message);
			//send message to UI
			model.addAttribute("message",message);
			log.info("Form backing for Sale Order model");
			model.addAttribute("saleOrder",new SaleOrder());
			addDropDownUI(model);
		} catch (Exception e) {
			log.error("Unable to save::"+e.getMessage());
			e.printStackTrace();
		}
		log.info("Back to UI page");
		//Goto UI page
		return "SaleOrderRegister";
	}
	
	//3.Show all data
	@GetMapping("/all")
	public String fetchAll(Model model) {
		List<SaleOrder> list=null;
		
		log.info("Entered Into fetchAll  method");
		try {
			list=service.getAllSaleOrders();
			model.addAttribute("list",list);
			addDropDownUI(model);
			log.info("Got Data from DB using service:size of list is:"+list.size());
		} catch (Exception e) {
			log.error("Unable to fetch Data from DB:"+e.getMessage());
			e.printStackTrace();
		}
		log.info("Back to UI page For data Display");
		return "SaleOrderData";
	}
	
	//4.Delete data by id
	@GetMapping("/delete/{id}")
	public String removeById(@PathVariable Integer id,Model model) {
		String msg=null;
		List<SaleOrder> list=null;
		String page=null;
		
		log.info("Enter into Delete Method to delete with id:"+id);
		try {
			if (service.isSaleOrderExist(id)) {
				service.deleteSaleOrder(id);;
				msg="Sale Order "+id+" deleted";
				log.debug(msg);
			} else {
				msg="Sale Order "+id+" Not exist";
				log.warn(msg);
			}
			model.addAttribute("message",msg);
			log.info("Fetching new Data after Delete");
			//Show other rows
			list=service.getAllSaleOrders();
			model.addAttribute("list",list);
			 page="SaleOrderData";
		} catch (Exception e) {
			log.error("Unable to Perform Delete Operation::"+e.getMessage());
			e.printStackTrace();
		}
		log.info("Back to UI data display page");
		return page;
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
		Optional<SaleOrder> opt=null;
		SaleOrder saleOrder=null;
		
		log.info("Entered into EDit operation with id:"+id);
		try {
			
			opt=service.getOneSaleOrder(id);
			log.info("Service called is Made");
			if (opt.isPresent()) {
				log.info("Data exist with id:"+id);
				saleOrder=opt.get();
				//Form backing object with data
				model.addAttribute("saleOrder",saleOrder);
				addDropDownUI(model);
				page="SaleOrderEdit";
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
	public String update(@ModelAttribute SaleOrder saleOrder,
						Model model) {
		String msg=null;
		List<SaleOrder> list=null;
		
		log.info("Enterd into Update operaion ");
		try {
			service.updateSaleOrder(saleOrder);;
			msg="Sale Order "+ saleOrder.getId() +" Updated";
			model.addAttribute("message",msg);
			log.debug(msg);
			
			log.info("Fetching latest Data");
			//new data from DB
			list=service.getAllSaleOrders();
			model.addAttribute("list",list);
		} catch (Exception e) {
			log.error("Unable to update data"+e.getMessage());
			e.printStackTrace();
		}
		log.info("Back to UI page");
		return "SaleOrderData";
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
		List<SaleOrder> list=null;
		
		try {
			log.info("Enterd into Export Data to excel file method");
			mav=new ModelAndView();
			mav.setView(new SaleOrderExcelView());
			
			//Send data to View Class
			log.info("Send data to view class");
			list=service.getAllSaleOrders();
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
	public ModelAndView exportToExcelOne(@PathVariable Integer id) {
		
		ModelAndView mav=null;
		Optional<SaleOrder> opt=null;
		
		try {
			log.info("Enter into excel with id"+id);
			mav=new ModelAndView();
			mav.setView(new SaleOrderExcelView());
			opt=service.getOneSaleOrder(id);
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
			List<SaleOrder> list=null;
			
			try {
				log.info("Enterd into Export Data to PDF file method");
				mav=new ModelAndView();
				mav.setView(new SaleOrderPdfView());
				
				//get data from service
				log.info("Send data to view class");
				list=service.getAllSaleOrders();
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
			Optional<SaleOrder> opt=null;
			
			try {
				log.info("Enter into PDF with id"+id);
				mov=new ModelAndView();
				mov.setView(new SaleOrderPdfView());
				
				//Get data from service
				log.info("Get One  Data from service");
				opt=service.getOneSaleOrder(id);
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
/**
 * 		@GetMapping("/charts")
 * 
		public String generateCharts() {
			List<Object[]> list=null;
			String location=null;
			
			try {
				log.info("Enter Into generate Chart and show data");
				//Data toShow at chart
				list=service.getOrderCodeCount();
				
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
			return "SaleOrderCharts";
		}
 */	
		//----------------------------------------------Additional Client Side Form validation Based on Shipment Code----------------------------
		//---AJAX VALIDATION----------
			//.. /SaleOrder/validatecode?code=ABCD
/**
 * 			@GetMapping("/validatecode")
 * 			@param SaleOrderCode
			public @ResponseBody String validatePartCode(@RequestParam String SaleOrderCode) 
			{
				String message="";
				
				try {
					log.info("Enter into the Part code validation"+SaleOrderCode);
					if(service.isSaleOrderCodeExist(SaleOrderCode)) {
						message = "Purchase Order Code <b>'"+SaleOrderCode+"' Already exist</b>!";
						log.debug(message);
					}
				} catch (Exception e) {
					log.error("Unable to validate code"+e.getMessage());
					e.printStackTrace();
				}
				log.info("Return the message to UI page"+message);
				return message;
			}
*/		
		//===================For INBOUND Operations To design Screen 2 to Read PO-ID=============================================
			@GetMapping("/dtls/{id}")
			public String showDetails(@PathVariable Integer id,//For PO id
									Model model) {
				String page=null;
				Optional<SaleOrder> so=null;
				SaleDetail saleDtl=null;
				
				try {
					log.info("Enter into the show details with id "+id);
					so=service.getOneSaleOrder(id);
					saleDtl=new SaleDetail();
					if (so.isPresent()) {
						model.addAttribute("so",so.get());
						saleDtl.setSo(so.get());
						model.addAttribute("saleDtl",saleDtl);
						 //Display All Added Items as HTML table
						  model.addAttribute("dtlList",service.getSaleDtlWithSoId(so.get().getId()));
						 
						addDorpDownUiForDtls(model);
						log.info("Get Po object to UI page");
						page="SaleDetails";
					} else {
						log.info("Back to UI page All");
						page="redirect:../all";
					}
				} catch (Exception e) {
					log.error("Enable to Read PO "+e.getMessage());
					e.printStackTrace();
				}
				log.info("Return to UI page ");
				return page;
			}
			
			//2. on click add button 
			/**
			 * Read PurchaseDtl object and save DB
			 * redirect to /dtls/{id} -> showDtl() method
			 */
			@PostMapping("/addItem")
			public String addItemToSo(
					@ModelAttribute SaleDetail saleDtl) 
			{
				service.addItemToSo(saleDtl);
				service.updateSaleOrderStatus("PICKING",saleDtl.getSo().getId());
				return "redirect:dtls/"+saleDtl.getSo().getId();
			}
			
			//3.For remove button
			@GetMapping("/removePart")
			public String removePart(@RequestParam Integer dtlId,
									@RequestParam Integer soId) {
				
				Integer dtlCount=0;
				try {
					log.info("Enter to removePart method with id "+dtlId);
					service.deleteSaleDtl(dtlId);;
					dtlCount=service.getSaleDtlCountWithSoId(soId);
					if (dtlCount==0) {
						service.updateSaleOrderStatus("OPEN",soId);
					}
				} catch (Exception e) {
					log.error("Unable to delete parts "+e.getMessage());
					e.printStackTrace();
				}
				log.info("Return purchase details page");
				return "redirect:dtls/"+soId;//POID
			}
			
			//4. On click conform button change status from PICKING to ORDERED
			@GetMapping("/conformOrder/{id}")
			public String placeOrder(@PathVariable Integer id)
			{
				Integer dtlCount = service.getSaleDtlCountWithSoId(id);
				if(dtlCount>0) {
					service.updateSaleOrderStatus("ORDERED",id);
				}
				return "redirect:../dtls/"+id; //POID
			}
			
			//5. chnage status from ORDERED to INVOICED
			@GetMapping("/invoiceOrder/{id}")
			public String invoiceOrder(@PathVariable Integer id)
			{
				service.updateSaleOrderStatus("INVOICED",id);
				return "redirect:../all"; //POID
			}


			//6. chnage status from ORDERED to INVOICED
			@GetMapping("/printInvoice/{id}")
			public ModelAndView printInvoice(@PathVariable Integer id)
			{
				ModelAndView mov = new ModelAndView();
				mov.setView(new CustomerInvoicePdfView());
				mov.addObject("so", service.getOneSaleOrder(id).get());
				return mov;
			}

	}
