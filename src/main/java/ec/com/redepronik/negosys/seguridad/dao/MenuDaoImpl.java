package ec.com.redepronik.negosys.seguridad.dao;

import java.io.Serializable;

import org.springframework.stereotype.Repository;

import ec.com.redepronik.negosys.seguridad.entity.Menu;
import ec.com.redepronik.negosys.utils.dao.GenericDaoImpl;

@Repository
public class MenuDaoImpl extends GenericDaoImpl<Menu, Integer> implements
		MenuDao, Serializable {

	private static final long serialVersionUID = 1L;

}
