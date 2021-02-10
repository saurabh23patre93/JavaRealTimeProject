package in.nit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import in.nit.model.Grn;
import in.nit.service.IGrnService;
import in.nit.service.IPurchaseOrderService;

@Controller
@RequestMapping("/grn")
public class GrnController {
	@Autowired
	private IGrnService service;
	
	//For Model integration
	@Autowired
	private IPurchaseOrderService poservice;
	
	//for Model intergration
	private void addDropDownUI(Model model) {
		model.addAttribute("pos",poservice.getPoIdAndCodeByStatus("INVOICED"));
	}
	
	//1.Show Register Page
	@GetMapping("/register")
	public String showGrnRegister(Model model) {
		
		model.addAttribute("grn",new Grn());
		addDropDownUI(model);
		return "GrnRegister";
	}
}
