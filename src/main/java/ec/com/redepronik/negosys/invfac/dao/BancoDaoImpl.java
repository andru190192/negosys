package ec.com.redepronik.negosys.invfac.dao;

import org.springframework.stereotype.Repository;

import ec.com.redepronik.negosys.invfac.entity.Banco;
import ec.com.redepronik.negosys.utils.dao.GenericDaoImpl;

@Repository
public class BancoDaoImpl extends GenericDaoImpl<Banco, Integer> implements
		BancoDao {

}