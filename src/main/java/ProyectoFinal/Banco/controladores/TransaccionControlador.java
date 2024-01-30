package ProyectoFinal.Banco.controladores;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import ProyectoFinal.Banco.dao.Transaccion;
import ProyectoFinal.Banco.dto.TransaccionDTO;
import ProyectoFinal.Banco.servicios.ITransaccionServicio;
import ProyectoFinal.Banco.servicios.Util;


@Controller
public class TransaccionControlador {

	@Autowired
	private ITransaccionServicio transaccionServicio;
	
	@GetMapping("/privada/nuevaTransaccion")
	public String nuevaTrasaccion(Model model, Authentication authentication) {
		return "nuevaTransaccion";
	}
	
	@PostMapping("/privada/nuevaTransaccion")
	public String nuevaTransaccionPost(@ModelAttribute TransaccionDTO transaccionDTO, Model model) {

		
		Transaccion transaccionDao=Util.transaccionToDao(transaccionDTO);
		
		Transaccion nuevaTransaccion = transaccionServicio.registrarTransaccion(transaccionDao);	
		
		return "nuevaTransaccion";
	}
}
