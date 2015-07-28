package ec.com.redepronik.negosys.invfac.dao;

import org.springframework.stereotype.Repository;

import ec.com.redepronik.negosys.invfac.entity.LocalCajero;
import ec.com.redepronik.negosys.utils.dao.GenericDaoImpl;

@Repository
public class LocalEmpleadoCargoDaoImpl extends
		GenericDaoImpl<LocalCajero, Integer> implements LocalEmpleadoCargoDao {

}