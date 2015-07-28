package ec.com.redepronik.negosys.seguridad.dao;

import java.io.Serializable;

import org.springframework.stereotype.Repository;

import ec.com.redepronik.negosys.seguridad.entity.Bitacora;
import ec.com.redepronik.negosys.utils.dao.GenericDaoImpl;

@Repository
public class BitacoraDaoImpl extends GenericDaoImpl<Bitacora, Integer>
		implements BitacoraDao, Serializable {

	private static final long serialVersionUID = 1L;

}
