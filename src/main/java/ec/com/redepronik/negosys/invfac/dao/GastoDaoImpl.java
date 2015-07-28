package ec.com.redepronik.negosys.invfac.dao;

import org.springframework.stereotype.Repository;

import ec.com.redepronik.negosys.invfac.entity.Gastos;
import ec.com.redepronik.negosys.utils.dao.GenericDaoImpl;

@Repository
public class GastoDaoImpl extends GenericDaoImpl<Gastos, Integer> implements
		GastoDao {

}