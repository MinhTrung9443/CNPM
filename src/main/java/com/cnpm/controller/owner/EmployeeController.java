package com.cnpm.controller.owner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.cnpm.entity.Account;
import com.cnpm.entity.Employee;
import com.cnpm.entity.Role;
import com.cnpm.service.interfaces.IEmployeeService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/owner/account")
public class EmployeeController {
	@Autowired
	IEmployeeService eService;

	@RequestMapping("")
	public String getEmployee(Model model, @RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size) {

		int count = (int) eService.count();
		int currentPage = page.orElse(1);
		int pageSize = size.orElse(3);

		Pageable pageable = PageRequest.of(currentPage - 1, pageSize);

		int totalPages = eService.findAll(pageable).getTotalPages();
		Page<Employee> list = eService.findAll(pageable);
		int start = Math.max(1, currentPage - 2);
		int end = Math.min(currentPage + 2, totalPages);

		if (totalPages > count) {
			if (end == totalPages)
				start = end - count;
			else if (start == 1)
				end = start + count;
		}
		List<Integer> pageNumbers = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());

		model.addAttribute("list", list);
		model.addAttribute("currentPage", currentPage);
		model.addAttribute("totalPages", totalPages);
		model.addAttribute("pageNumbers", pageNumbers);
		return "owner/account/list";

	}

	@GetMapping("/add")
	public String add(Model model) {
		Employee employee = new Employee();
		Account account = new Account();
		employee.setAccount(account);
		model.addAttribute("employee", employee);

		return "owner/account/add";
	}
	
	@PostMapping("/save")
	public ModelAndView saveOrUpdate(ModelMap model, @Valid @ModelAttribute Employee employee, BindingResult result,
			@Valid @ModelAttribute Account account) {
		if (result.hasErrors()) {
			model.addAttribute("employee", employee);
			if (employee.getUserId() != null) {
				return new ModelAndView("owner/account/edit", model);
			} else {
				return new ModelAndView("owner/account/add", model);
			}
		}
		
		Long id = employee.getUserId();
		boolean check = false;

		if (eService.findByEmail(employee.getEmail()) != null) {
			if (id != null) {
				if (eService.findByEmail(employee.getEmail()).getUserId() != id) {
					model.addAttribute("existEmail", "Email này đã tồn tại!!!");
					check = true;
				}
			} else {
				model.addAttribute("existEmail", "Email đã tồn tại!!!!");
				check = true;
			}
		}

		String phoneRegex = "^\\d{10}$";
		if (!Pattern.matches(phoneRegex, employee.getPhone())) {
			model.addAttribute("phoneErr", "Số điện thoại không hợp lệ!!!");
			check = true;
		}

		if (eService.findByPhone(employee.getPhone()) != null) {
			if (id != null) {
				if (eService.findByPhone(employee.getPhone()).getUserId() != id) {
					model.addAttribute("existPhone", "Số điện thoại đã tồn tại!!!");
					check = true;
				}
			} else {
				model.addAttribute("existPhone", "Số điện thoại đã tồn tại!!!!");
				check = true;
			}

		}
		
		if (eService.findByUsername(account.getUsername()) != null) {
			if (id == null) {
				model.addAttribute("existUsername", "Username đã tồn tại!!!!");
				check = true;
			} else {
				account = eService.findByUsername(account.getUsername());
			}
		}

		if (check) {
			Employee entity = new Employee();
			BeanUtils.copyProperties(employee, entity);
			entity.setAccount(account);
			model.addAttribute("employee", entity);
			if (entity.getUserId() != null) {
				return new ModelAndView("owner/account/edit", model);
			} else {
				return new ModelAndView("owner/account/add", model);
			}
		}
		Employee entity = new Employee();
		Account tk = new Account();
		BeanUtils.copyProperties(employee, entity);
		BeanUtils.copyProperties(account, tk);
		entity.setAccount(tk);
		tk.setRole(new Role((long) 2, "employee"));
		entity.setAccount(tk);
		eService.save(entity);
		String message = "";
		model.addAttribute("message", message);

		return new ModelAndView("forward:/owner/account", model);
	}

	@GetMapping("/edit/{id}")
	public ModelAndView edit(ModelMap model, @PathVariable("id") Long userId) {
		Optional<Employee> optEmployee = eService.findById(userId);
		Employee employee = new Employee();
		if (optEmployee.isPresent()) {
			Employee entity = optEmployee.get();

			BeanUtils.copyProperties(entity, employee);

			model.addAttribute("employee", employee);

			return new ModelAndView("owner/account/edit", model);
		}
		return new ModelAndView("forward:/owner/account", model);

	}
	@Transactional
	@GetMapping("/delete/{id}")
	public ModelAndView delete(ModelMap model, @PathVariable("id") Long userId) {
		Optional<Employee> optEmployee = eService.findById(userId);
		if (optEmployee.isPresent()) {
			
			Account acc = optEmployee.get().getAccount();
			Long accountid = acc.getAccountId();
			
			System.out.println(accountid);
			eService.deleteByAccountId(accountid);
			
			eService.deleteById(userId);
			
			List<Employee> list = eService.findAll();
			model.addAttribute("list", list);
			return new ModelAndView("forward:/owner/account", model);
		}
		model.addAttribute("message", "Employee is not existed!!");
		return new ModelAndView("forward:/owner/account", model);
	}

	@GetMapping("/search")
	public String search(Model model, @RequestParam(name = "userId", required = false) Long userId) {
	    List<Employee> employeeList;
	    
	    System.out.println(userId);
	    if (userId != null) {
	        Optional<Employee> employee = eService.findById(userId);
	        if (employee.isPresent()) {
	            employeeList = List.of(employee.get());
	        } else {
	            employeeList = new ArrayList<>(); 
	            model.addAttribute("err","Nhân viên này không tồn tại");
	        }
	        model.addAttribute("userId", userId); 
	    } else {
	        employeeList = eService.findAll(); 
	    }
	    model.addAttribute("list", employeeList); 
	    return "owner/account/search"; 
	}
}
