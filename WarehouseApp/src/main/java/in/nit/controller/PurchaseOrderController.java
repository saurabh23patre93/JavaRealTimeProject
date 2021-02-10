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

import in.nit.model.PurchaseDetail;
import in.nit.model.PurchaseOrder;
import in.nit.service.IPartService;
import in.nit.service.IPurchaseOrderService;
import in.nit.service.IShipmentTypeService;
import in.nit.service.IWhUserTypeService;
import in.nit.util.PurchaseOrderUtil;
import in.nit.view.PartExcelView;
import in.nit.view.PurchaseOrderExcelView;
import in.nit.view.PurchaseOrderPdfView;
import in.nit.view.VendorInvoicePdfView;

@Controller
@RequestMapping("/purchaseorder")
public class PurchaseOrderController {
	//Create Logger Object
	private Logger log=LoggerFactory.getLogger(PurchaseOrderController.class);	
	
	//a.HAS-A relation With IUomService
	@Autowired
	private IPurchaseOrderService service;
	//a.HAS-A relation With IShipmentTypeService
	@Autowired
	private IShipmentTypeService shipmentTypeService;
	//a.HAS-A relation With IWhUserTypeService
	@Autowired
	private IWhUserTypeService whUserTypeService;
	
	//HAS-A relation with IPartService to achieve Inbounds operation
	@Autowired
	private IPartService partService;
	
	//For PIE And BAR chart
	@Autowired
	private PurchaseOrderUtil util;
	
	@Autowired
	private ServletContext context;
	
	
	/**b.Get DropDown Map Data to achive inbound operation
	 *call this method inside other controller methods
	 *where return page is Register or edit 		
	**/
	private void addDorpDownUiForDtls(Model model) {
		model.addAttribute("parts", partService.getPartIdAndCode());
	}	
	/**b.Get DropDown Map Data
	 *call this method inside other controller methods
	 *where return page is Register or edit 		
	**/
	private void addDropDownUI(Model model) {
		model.addAttribute("shipmentTypes",shipmentTypeService.getShipmentTypeIdAndModel());
		model.addAttribute("whUserTypes",whUserTypeService.getWhUserTypeIdAndCode("Vendor"));
	}
	
	//1.Show Register Page
	/**
	 * URL:/register
	 * Goto Page ParchaseOrderRegister.html
	 */
	@GetMapping("/register")
	public String showRegister(Model model) {
		//Form Backing Object
		model.addAttribute("purchaseOrder", new PurchaseOrder());
		addDropDownUI(model);
		return "PurchaseOrderRegister";
	}
	
	//2.save:on click submit
	/**
	 * URL: /save, Type: POST
	 * Goto : ParchaseOrderRegister
	 */
	@PostMapping("/save")
	public String save(//Read from Data from Ui
						@ModelAttribute PurchaseOrder parchaseOrder,
						Model model//to send data to UI
						) {
		
		Integer id=null;
		String message=null;
		
		log.info("Enter Into  Saved method");
		try {
			//Perform Save operation
			id=service.savePurchaseOrder(parchaseOrder);
			//Construct one message
			message="Purchase Order "+id +" saved successfully";
			log.debug(message);
			//send message to UI
			model.addAttribute("message",message);
			log.info("Form backing for Purchase Order model");
			model.addAttribute("purchaseOrder",new PurchaseOrder());
			addDropDownUI(model);
		} catch (Exception e) {
			log.error("Unable to save::"+e.getMessage());
			e.printStackTrace();
		}
		log.info("Back to UI page");
		//Goto UI page
		return "PurchaseOrderRegister";
	}
	
	//3.Show all data
	@GetMapping("/all")
	public String fetchAll(Model model) {
		List<PurchaseOrder> list=null;
		
		log.info("Entered Into fetchAll Part method");
		try {
			list=service.getAllPurchaseOrders();
			model.addAttribute("list",list);
			addDropDownUI(model);
			log.info("Got Data from DB using service:size of list is:"+list.size());
		} catch (Exception e) {
			log.error("Unable to fetch Data from DB:"+e.getMessage());
			e.printStackTrace();
		}
		log.info("Back to UI page For data Display");
		return "PurchaseOrderData";
	}
	
	//4.Delete data by id
	@GetMapping("/delete/{id}")
	public String removeById(@PathVariable Integer id,Model model) {
		String msg=null;
		List<PurchaseOrder> list=null;
		
		log.info("Entered into Delete Method to delete with id:"+id);
		try {
			if (service.isPurchaseOrderExist(id)) {
				service.deletePurchaseOrder(id);;
				msg="Purchase Order "+id+" deleted";
				log.debug(msg);
			} else {
				msg="Purchase order "+id+" Not exist";
				log.warn(msg);
			}
			model.addAttribute("message",msg);
			log.info("Fetching new Data after Delete");
			//Show other rows
			list=service.getAllPurchaseOrders();
			model.addAttribute("list",list);
		} catch (Exception e) {
			log.error("Unable to Perform Delete Operation::"+e.getMessage());
			e.printStackTrace();
		}
		log.info("Back to UI data display page");
		return "PurchaseOrderData";
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
		Optional<PurchaseOrder> opt=null;
		PurchaseOrder purchaseOrder=null;
		
		log.info("Entered into EDit operation with id:"+id);
		try {
			
			opt=service.getOnePurchaseOrder(id);
			log.info("Service called is Made");
			if (opt.isPresent()) {
				log.info("Data exist with id:"+id);
				purchaseOrder=opt.get();
				//Form backing object with data
				model.addAttribute("purchaseOrder",purchaseOrder);
				addDropDownUI(model);
				page="PurchaseOrderEdit";
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
	public String update(@ModelAttribute PurchaseOrder purchaseOrder,
						Model model) {
		String msg=null;
		List<PurchaseOrder> list=null;
		
		log.info("Enterd into Update operaion ");
		try {
			service.updatePurchaseOrder(purchaseOrder);;
			msg="Purchase Order "+ purchaseOrder.getId() +" Updated";
			model.addAttribute("message",msg);
			log.debug(msg);
			
			log.info("Fetching latest Data");
			//new data from DB
			list=service.getAllPurchaseOrders();
			model.addAttribute("list",list);
		} catch (Exception e) {
			log.error("Unable to update data"+e.getMessage());
			e.printStackTrace();
		}
		log.info("Back to UI page");
		return "PurchaseOrderData";
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
		List<PurchaseOrder> list=null;
		
		try {
			log.info("Enterd into Export Data to excel file method");
			mav=new ModelAndView();
			mav.setView(new PurchaseOrderExcelView());
			
			//Send data to View Class
			log.info("Send data to view class");
			list=service.getAllPurchaseOrders();
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
		Optional<PurchaseOrder> opt=null;
		
		try {
			log.info("Enter into excel with id"+id);
			mav=new ModelAndView();
			mav.setView(new PartExcelView());
			opt=service.getOnePurchaseOrder(id);
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
			List<PurchaseOrder> list=null;
			
			try {
				log.info("Enterd into Export Data to PDF file method");
				mav=new ModelAndView();
				mav.setView(new PurchaseOrderPdfView());
				
				//get data from service
				log.info("Send data to view class");
				list=service.getAllPurchaseOrders();
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
			Optional<PurchaseOrder> opt=null;
			
			try {
				log.info("Enter into PDF with id"+id);
				mov=new ModelAndView();
				mov.setView(new PurchaseOrderPdfView());
				
				//Get data from service
				log.info("Get One  Data from service");
				opt=service.getOnePurchaseOrder(id);
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
				list=service.getOrderCodeCount();
				
				//Dynamic Temp folder location for service instance
				location=context.getRealPath("/");
				log.info("CHART LOCATION IS: " + location );
				
				//call chart methods
				util.generatePieChart(location,list);
				util.generateBarChart(location,list);
			} catch (Exception e) {
				log.error("Enable to OPen charts " +e.getMessage());
			}
			
			log.info("Return to UI page Of Charts");
			return "PurchaseOrderCharts";
		}
		
		//----------------------------------------------Additional Client Side Form validation Based on Shipment Code----------------------------
		//---AJAX VALIDATION----------
			//.. /purchaseorder/validatecode?code=ABCD
			@GetMapping("/validatecode")
			public @ResponseBody String validatePartCode(@RequestParam String purchaseOrderCode) 
			{
				String message="";
				
				try {
					log.info("Enter into the Part code validation"+purchaseOrderCode);
					if(service.isPurchaseOrderCodeExist(purchaseOrderCode)) {
						message = "Purchase Order Code <b>'"+purchaseOrderCode+"' Already exist</b>!";
						log.debug(message);
					}
				} catch (Exception e) {
					log.error("Unable to validate code"+e.getMessage());
					e.printStackTrace();
				}
				log.info("Return the message to UI page"+message);
				return message;
			}
		
			//*************************************************************************//
			//**                    SCREEN#2 OPERATIONS                             ***//
			//*************************************************************************//
		//===================For INBOUND Operations To design Screen 2 to Read PO-ID=============================================
			@GetMapping("/dtls/{id}")
			public String showDetails(@PathVariable Integer id,//For PO id
									Model model) {
				String page=null;
				Optional<PurchaseOrder> po=null;
				PurchaseDetail purchaseDtl=null;
				try {
					log.info("Enter into the show details with id "+id);
					po=service.getOnePurchaseOrder(id);
					if (po.isPresent()) {
						model.addAttribute("po",po.get());
						//It will show PartsDropDown
						addDorpDownUiForDtls(model);
						//form backing Object for Adding PART + Linked with PO
						purchaseDtl=new PurchaseDetail();
						purchaseDtl.setPo(po.get());
						log.info("Adding part and linked with PO "+po.get());
						model.addAttribute("purchaseDtl", purchaseDtl);
						
						//Display All Added Parts as HTML table
						model.addAttribute("dtlList",service.getPurchaseDetailWithPoId(po.get().getId()));
						  
						log.info("Get Po object to UI page");
						page="PurchaseDetails";
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
			
			//2.On click add button
			/**
			 * Read PurchaseDtl object and save DB
			 * redirect to /dtls/{id} -> showDtl() method
			 */
			@PostMapping("/addPart")
			public String addPartToPo(@ModelAttribute PurchaseDetail purchaseDtl) {
				Integer poId=null;
//				try {
//					log.info("Enter to Add part method with id"+purchaseDtl);
					service.addPartToPo(purchaseDtl);
					poId=purchaseDtl.getPo().getId();
					log.info("Update Purchase order status when PICKING");
					service.updatePurchaseOrderStatus("PICKING",poId);
//				} catch (Exception e) {
//					log.error("Unable to save part "+e.getMessage());
//					e.printStackTrace();
//				}
				
				log.info("Back to details page");
				return "redirect:dtls/"+poId;
			}
			
			//3.For remove button
			@GetMapping("/removePart")
			public String removePart(@RequestParam Integer dtlId,
									@RequestParam Integer poId) {
				
				Integer dtlCount=0;
				try {
					log.info("Enter to removePart method with id "+dtlId);
					service.deletePurchaseDetail(dtlId);
					dtlCount=service.getPurchaseDetailCountWithPoId(poId);
					if (dtlCount==0) {
						service.updatePurchaseOrderStatus("OPEN",poId);
					}
				} catch (Exception e) {
					log.error("Unable to delete parts "+e.getMessage());
					e.printStackTrace();
				}
				log.info("Return purchase details page");
				return "redirect:dtls/"+poId;//POID
			}
			
			/**
			 * On click Conform Button Status must 
			 * be changed from PICKING to ORDERED.
			 */
			//4.On click Conform Button Status must be changed from PICKING to ORDERED.
			@GetMapping("/conformOrder/{id}")
			public String placeOrder( @PathVariable Integer id) {
				Integer dtlCount=0;
//				try {
					log.info("Enter into place order with id "+id);
					dtlCount=service.getPurchaseDetailCountWithPoId(id);
					if (dtlCount>0) {
						log.info("Update the Purchase Order when orderd");
						service.updatePurchaseOrderStatus("ORDERED", id);
					}
//					
//				} catch (Exception e) {
//					log.error("Unable to place order "+e.getMessage());
//					e.printStackTrace();
//				}
				log.info("Back to all Page");
				return "redirect:../dtls/"+id;//POID
			}
			
			//5. change status from ORDERED to INVOICED
			@GetMapping("/printOrder/{id}")
			public String invoiceOrder(@PathVariable Integer id) {
//				try {
					log.info("Enter into invoice order with id "+id);
					service.updatePurchaseOrderStatus("INVOICED", id);
//				} catch (Exception e) {
//					log.error("Unable to Invoice order with id "+e.getMessage());
//					e.printStackTrace();
//				}
				return "redirect:../all";///POID
			}
			
			//6. chnage status from ORDERED to INVOICED
			@GetMapping("/printInvoice/{id}")
			public ModelAndView printInvoice(@PathVariable Integer id)
			{
				ModelAndView m = new ModelAndView();
				m.setView(new VendorInvoicePdfView());
				m.addObject("po", service.getOnePurchaseOrder(id));
				return m;
			}
			
			
	}

