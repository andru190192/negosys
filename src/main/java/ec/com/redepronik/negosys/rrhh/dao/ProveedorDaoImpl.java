package ec.com.redepronik.negosys.rrhh.dao;

import java.io.Serializable;

import org.springframework.stereotype.Repository;

import ec.com.redepronik.negosys.rrhh.entity.Proveedor;
import ec.com.redepronik.negosys.utils.dao.GenericDaoImpl;

@Repository
public class ProveedorDaoImpl extends GenericDaoImpl<Proveedor, Integer>
		implements ProveedorDao, Serializable {

	private static final long serialVersionUID = 1L;

}