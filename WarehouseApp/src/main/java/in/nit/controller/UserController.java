package in.nit.controller;

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

import in.nit.model.Uom;
import in.nit.model.User;
import in.nit.service.IUserService;

@Controller
@RequestMapping("/user")
public class UserController {
	
	//Create Logger Object
	private Logger log=LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	private IUserService service;
	
	/**
	 * 1. On Enter/Register URL
	 * 	  Display UserRegister.html
	 * 	  Page in browser	
	 */
	@GetMapping("/register")
	public String showRegister(Model model) {
		model.addAttribute("user",new User());
		return "UserRegister";
	}
	
	/**
	 * 2.  On Click Submit(Register),
	 *     Read form(ModelAttribute)
	 *     save in DB using service
	 *     return message to UI
	 *     URL:/save+POST
	 */
	@PostMapping("/save")
	public String saveUser(@ModelAttribute User user,
						Model model) {
		Integer id=0;
		String message=null;
		
		log.info("Entered into User Saved Method");
		try {
			log.info("Operation to save data into DB::"+user);
			//Perform Save operation
			id=service.saveUser(user);
			log.info("Operation to save data into DB::"+user +id);
			//Construct one message
			message="User "+id +" saved successfully";
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
		return "UserRegister";
	}
	
	/***
	 * 3. Fetch all users from DB
	 */
	@GetMapping("/all")//For pagination purpose
	public String fetchAll(@PageableDefault(page = 0,size = 3) Pageable pageable,
							Model model) {
		Page<User> page=null;
		
		log.info("Enter into fetchAll USER method");
		try {
			page=service.getAllUsers(pageable);
			model.addAttribute("page", page);
			log.info("Got data from DB using service :size of list is:"+page);
		} catch (Exception e) {
			log.error("Unable to fetch Data form DB::"+e.getMessage());
			e.printStackTrace();
		}
		log.info("Back to UI page for Data display");
		return "UserData";
	}
	
	
	//4.Delete Data using by id
		@GetMapping("/delete/{id}")
		public String removeById(@PathVariable Integer id,
								@PageableDefault(page = 0,size = 3)Pageable pageable,
								Model model) {
			String msg=null;
			Page<User> page=null;
			
			log.info("Enter into delete method to delete with id"+id);
			try {
				if (service.isUserExist(id)) {
					try {
						service.deleteUser(id);
						msg="USER "+id+" is deleted";
						log.debug(msg);
					} catch (DataIntegrityViolationException dive) {
						msg = "USER '" + id + "' can not be deleted! It is used by Part";
						log.error("Unable to delete  "+dive.getMessage());
						dive.printStackTrace();
					}
				} else {
					msg="USER "+id+" is not exist.";
					log.warn(msg);
				}
				model.addAttribute("message",msg);
				log.info("Fetching new Data afete Delete");
				//show all data
				page=service.getAllUsers(pageable);
				model.addAttribute("page",page);
			} catch (Exception e) {
				log.error("Unable to perform Delete Operation::"+e.getMessage());
				e.printStackTrace();
			}
			
			log.info("Back to UI data display page");
			return "UserData";
		}
	//==============================================================================================
	//for Status update
	//For Activate
	@GetMapping("/activate/{id}")
	public String activateUser(@PathVariable Integer id) {
		
		log.info("Enter into activateUser method");
		try {
			
			service.updateUserStatus(true,id);
		} catch (Exception e) {
			log.error("Unable to fetch Data form DB::"+e.getMessage());
			e.printStackTrace();
		}
		log.info("Back to UI page for Data display");
		return "redirect:../all";
	}
	
	//For IN Activate
		@GetMapping("/inactivate/{id}")
		public String deActivateUser(@PathVariable Integer id) {
			
			log.info("Enter into deActivateUser method");
			try {
				
				service.updateUserStatus(false,id);
			} catch (Exception e) {
				log.error("Unable to fetch Data form DB::"+e.getMessage());
				e.printStackTrace();
			}
			log.info("Back to UI page for Data display");
			return "redirect:../all";
		}
	
}
