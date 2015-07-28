package ec.com.redepronik.negosys.utils.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;

public class GenericDaoImpl<T, K extends Serializable> implements
		GenericDao<T, K> {

	@Autowired
	public SessionFactory sessionFactory;

	public void actualizar(T t) {
		session().update(t);
	}

	public Boolean comprobarIndices(Class<T> type, String atributo,
			String valor, String id) {
		T t = null;
		t = obtenerPorAtributo(type, atributo, valor, null);
		if (t != null) {
			Object o = t;
			String idOriginal;
			try {
				idOriginal = String.valueOf(o.getClass().getMethod("getId")
						.invoke(o));
				if (idOriginal.compareTo(id) == 0) {
					evict(t);
					return false;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return true;
		}
		return false;
	}

	public Boolean comprobarIndicesMinuscula(Class<T> type, String atributo,
			String valor, String id) {
		T t = null;
		t = obtenerPorAtributo(type, atributo, valor, null);
		if (t != null) {
			Object o = t;
			String idOriginal;
			try {
				idOriginal = String.valueOf(o.getClass().getMethod("getId")
						.invoke(o));
				if (idOriginal.compareTo(id) == 0) {
					evict(t);
					return false;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return true;
		}
		return false;
	}

	public Object contar(Class<T> type) {
		return session().createCriteria(type.getName())
				.setProjection(Projections.count("id")).uniqueResult();
	}

	public Object contar(String consulta, Object[] valores) {
		Query query = (Query) session().createQuery(consulta);
		if (valores != null) {
			for (Integer i = 0; i < valores.length; i++) {
				Integer iparameter = i + 1;
				query.setParameter(iparameter.toString(), valores[i]);
			}
		}
		return query.uniqueResult();
	}

	public void eliminar(T t) {
		session().delete(t);
	}

	@SuppressWarnings("unchecked")
	public void eliminarPorId(Class<T> type, K id) {
		T t = (T) session().load(type.getName(), id);
		session().delete(t);
	}

	public void evict(T t) {
		session().evict(t);
	}

	public void insertar(T t) {
		session().save(t);
	}

	@SuppressWarnings("unchecked")
	public List<T> obtener(Class<T> type, String atributoOrden, Boolean activo) {
		if (activo != null)
			return session().createCriteria(type.getName())
					.add(Restrictions.eq("activo", activo))
					.addOrder(Order.asc(atributoOrden)).list();
		else
			return session().createCriteria(type.getName())
					.addOrder(Order.asc(atributoOrden)).list();

	}

	@SuppressWarnings("unchecked")
	public List<T> obtenerListaPorAtributo(Class<T> type, String Atributo,
			String valorAtributo, Boolean activo) {
		if (activo != null)
			return session().createCriteria(type.getName())
					.add(Restrictions.eq(Atributo, valorAtributo))
					.add(Restrictions.eq("activo", activo)).list();
		else
			return session().createCriteria(type.getName())
					.add(Restrictions.eq(Atributo, valorAtributo)).list();

	}

	@SuppressWarnings("unchecked")
	public T obtenerPorAtributo(Class<T> type, String Atributo,
			String valorAtributo, Boolean activo) {
		if (activo != null)
			return (T) session().createCriteria(type.getName())
					.add(Restrictions.eq(Atributo, valorAtributo))
					.add(Restrictions.eq("activo", activo)).uniqueResult();
		else
			return (T) session().createCriteria(type.getName())
					.add(Restrictions.eq(Atributo, valorAtributo))
					.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	public List<T> obtenerPorHql(String consulta, Object[] valores) {
		List<T> list = null;
		Query query = (Query) session().createQuery(consulta);
		if (valores != null)
			for (int i = 0; i < valores.length; i++)
				if (valores[i] != null)
					query.setParameter(String.valueOf(i + 1), valores[i]);
		list = query.list();
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<T> obtenerPorHql(String consulta, Object[] valores, int min,
			int max) {
		List<T> list = null;
		Query query = (Query) session().createQuery(consulta)
				.setFirstResult(min).setMaxResults(max);
		if (valores != null)
			for (int i = 0; i < valores.length; i++)
				query.setParameter(String.valueOf(i + 1), valores[i]);

		list = query.list();
		return list;
	}

	@SuppressWarnings("unchecked")
	public T obtenerPorId(Class<T> type, K id) {
		return (T) session().get(type.getName(), id);
	}

	public Session session() {
		return sessionFactory.getCurrentSession();
	}
}