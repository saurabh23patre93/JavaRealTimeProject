package in.nit.controller;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.standard.expression.OrExpression;

import in.nit.model.OrderMethod;
import in.nit.model.ShipmentType;
import in.nit.service.IOrderMethodService;
import in.nit.specification.OrderMethodSpecification;
import in.nit.view.ShipmentTypePdfView;

@Controller
@RequestMapping("/ordermethod")
public class OrderMethodController {
	//Create Logger Object
	private Logger log=LoggerFactory.getLogger(PartController.class);

	@Autowired
	private IOrderMethodService service;
	
	//1.Show Register Page
	/**
	 * URL:/register
	 * Goto Page OrderMethodRegister.html
	 */
	@GetMapping("/register")
	public String showRegister(Model model) {
		model.addAttribute("orderMethod",new OrderMethod());
		return "OrderMethodRegister";
	}
	//2.Save:On click submit
	/**
	 * URL:/save, Type:POST
	 * Goto:OrderMethod
	 */
	@PostMapping("/save")
	public String save(//Read form data from UI
						@ModelAttribute OrderMethod orm,
						Model model//to send data to UI
						) {
		String id=null;
		String message=null;
		
		//Perform Save Operation
		id=service.saveOrderMethod(orm);
		//Construct one Message
		message="Order Method "+id+" saved Successfully";
		//Send message to UI
		model.addAttribute("message",message);
		//Goto Register page
		return "OrderMethodRegister";
	}
	
	//3.Show all data
	@GetMapping("/all")
	public String fetchAll(@ModelAttribute OrderMethod orderMethod,Model model) {
		List<OrderMethod> list=null;
		Specification<OrderMethod> specification=null;
		
		specification=new OrderMethodSpecification(orderMethod);
		list=service.getAllOrderMethods(specification);
		model.addAttribute("list",list);
		model.addAttribute("orderMethod",orderMethod);//form backing object
		return "OrderMethodData";
	}
	
	//4.Delete Data using by id
	@GetMapping("/delete/{id}")
	public String removeById(@PathVariable String id,Model model) {
		String msg=null;
		List<OrderMethod> list=null;
		
		if (service.isOrderMethodExist(id)) {
			service.deleteOrderMethod(id);
			msg="Order Method "+id+" is deleted"; 
		} else {
			msg="Order Method "+id+" is not exist";
		}
		model.addAttribute("message", msg);
		//show all data
		list=service.getAllOrderMethods();
		model.addAttribute("list",list);
		return "OrderMethodData";
	}
	
	/**
	 * On click Edit hyperlink at UI,
	 * read one PathVariable and fetch data from
	 * service,if exist send to edit page
	 * else redirect to data page
	 */
	//5.Show edit page with data
	@GetMapping("/edit/{id}")
	public String showEdit(@PathVariable String id,Model model) {
		String page=null;
		Optional<OrderMethod> opt=null;
		OrderMethod orm=null;
		
		opt=service.getOneOrderMethod(id);
		if (opt.isPresent()) {
			orm=opt.get();
			//form backing object with data
			model.addAttribute("orm", orm);
			page="OrderMethodEdit";
		} else {
			//if id not exist
			page="redirect:../all";
		}
		
		return page;
	}
	
	/**
	 * On click update button,read form data and perform update operation
	 * send back to Data page with success message
	 */
	//6.Update ::on click update
	@PostMapping("/update")
	public String update(@ModelAttribute OrderMethod orm,
						Model model) {
		String msg=null;
		List<OrderMethod> list=null;
		
		service.updateOrderMethod(orm);
		msg="Order Method "+orm.getId()+" Updated";
		
		//new data from db
		list=service.getAllOrderMethods();
		model.addAttribute("list",list);
		return "OrderMethodData";
	}
	
	//9.Export all row to pdf file
		@GetMapping("/pdf")
		public ModelAndView exportPdfAll() {
			ModelAndView mav=null;
			List<OrderMethod> list=null;
			
			try {
				log.info("Enterd into Export Data to PDF file method");
				mav=new ModelAndView();
				mav.setView(new ShipmentTypePdfView());
				
				//get data from service
				log.info("Send data to view class");
				list=service.getAllOrderMethods();
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
			Optional<OrderMethod> opt=null;
			
			try {
				log.info("Enter into PDF with id"+id);
				mov=new ModelAndView();
				mov.setView(new ShipmentTypePdfView());
				
				//Get data from service
				log.info("Get One  Data from service");
				opt=service.getOneOrderMethod(id);
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
		
		//11.For view All data
		@GetMapping("/view/{id}")
		public String showView(@PathVariable String id,Model model) {
			Optional<OrderMethod> opt=null;
			OrderMethod orm=null;
			
			try {
				log.info("Making Service Call");
				opt=service.getOneOrderMethod(id);
				log.info("Reading Data from Optional object");
				orm=opt.get();
				model.addAttribute("ob",orm);
			} catch (NoSuchElementException e) {
				log.error(e.getMessage());
				e.printStackTrace();
			}
			 return "OrderMethodView";
		}
}


