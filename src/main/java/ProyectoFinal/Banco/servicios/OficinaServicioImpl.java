package ProyectoFinal.Banco.servicios;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ProyectoFinal.Banco.dao.Oficina;
import ProyectoFinal.Banco.repositorios.OficinaRepositorio;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class OficinaServicioImpl implements IOficinaServicio{

	@Autowired
    private OficinaRepositorio oficinaRepositorio;
	
	@Override
    public List<Oficina> obtenerOficinas() {
		return oficinaRepositorio.findAll();

    }
}
