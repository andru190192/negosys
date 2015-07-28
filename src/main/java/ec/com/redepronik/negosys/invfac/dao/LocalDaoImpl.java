package ec.com.redepronik.negosys.invfac.dao;

import org.springframework.stereotype.Repository;

import ec.com.redepronik.negosys.invfac.entity.Local;
import ec.com.redepronik.negosys.utils.dao.GenericDaoImpl;

@Repository
public class LocalDaoImpl extends GenericDaoImpl<Local, Integer> implements
		LocalDao {

}