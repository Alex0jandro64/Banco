package ProyectoFinal.Banco.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ProyectoFinal.Banco.dao.Transaccion;

public interface TransaccionRepository extends JpaRepository<Transaccion, Long>{

	 List<Transaccion> findByUsuarioTransaccionRemitenteIdOrUsuarioTransaccionDestinatarioId(Long remitenteId, Long destinatarioId);
	 
}