package ec.com.redepronik.negosys.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
// @EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private Environment env;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable().authorizeRequests()
				.antMatchers("/resources/**", "/views/seguridad/login.jsf")
				.permitAll().antMatchers("/templates/**")
				.access("isAuthenticated()")
				.antMatchers("/javax.faces.resource/**")
				.access("isAuthenticated()")

				.antMatchers("/views/directorio/cliente.jsf")
				.hasAnyAuthority("ADMI", "CAJA")
				.antMatchers("/views/directorio/proveedor.jsf")
				.hasAnyAuthority("ADMI", "BODE")
				.antMatchers("/views/directorio/reportes.jsf")
				.hasAnyAuthority("ADMI")

				.antMatchers("/views/inventario/producto.jsf")
				.hasAnyAuthority("ADMI", "BODE")
				.antMatchers("/views/inventario/listadoIngreso.jsf")
				.hasAnyAuthority("ADMI", "BODE")
				.antMatchers("/views/inventario/ingreso.jsf")
				.hasAnyAuthority("BODE")
				.antMatchers("/views/inventario/kardex.jsf")
				.hasAnyAuthority("ADMI", "CAJA", "BODE")
				.antMatchers("/views/inventario/listadoBajaInventario.jsf")
				.hasAnyAuthority("ADMI", "BODE")
				.antMatchers("/views/inventario/bajaInventario.jsf")
				.hasAnyAuthority("BODE")
				.antMatchers("/views/inventario/traspasoBodega.jsf")
				.hasAnyAuthority("BODE")
				.antMatchers("/views/inventario/reportes.jsf")
				.hasAnyAuthority("ADMI")

				.antMatchers("/views/facturacion/listadoFactura.jsf")
				.hasAnyAuthority("ADMI", "CAJA")
				.antMatchers("/views/facturacion/factura.jsf")
				.hasAnyAuthority("CAJA")
				.antMatchers("/views/facturacion/listadoNotaEntrega.jsf")
				.hasAnyAuthority("ADMI", "CAJA")
				.antMatchers("/views/facturacion/notaEntrega.jsf")
				.hasAnyAuthority("CAJA")
				.antMatchers("/views/facturacion/listadoCotizacion.jsf")
				.hasAnyAuthority("ADMI", "VEND")
				.antMatchers("/views/facturacion/cotizacion.jsf")
				.hasAnyAuthority("VEND")
				.antMatchers("/views/facturacion/listadoPedido.jsf")
				.hasAnyAuthority("ADMI", "VEND")
				.antMatchers("/views/facturacion/pedido.jsf")
				.hasAnyAuthority("VEND")
				.antMatchers("/views/facturacion/listadoNotaCredito.jsf")
				.hasAnyAuthority("ADMI", "CAJA")
				.antMatchers("/views/facturacion/listadoDevolucionVenta.jsf")
				.hasAnyAuthority("ADMI", "CAJA")
				.antMatchers("/views/facturacion/reportes.jsf")
				.hasAnyAuthority("ADMI")

				.antMatchers("/views/cuentasPorPagar/pago.jsf")
				.hasAnyAuthority("ADMI", "CAJA")
				.antMatchers("/views/cuentasPorPagar/reportes.jsf")
				.hasAnyAuthority("ADMI", "CAJA")

				.antMatchers("/views/cuentasPorCobrar/cobro.jsf")
				.hasAnyAuthority("ADMI", "CAJA")
				.antMatchers("/views/cuentasPorCobrar/credito.jsf")
				.hasAnyAuthority("ADMI", "CAJA")
				.antMatchers("/views/cuentasPorCobrar/reportes.jsf")
				.hasAnyAuthority("ADMI", "CAJA")

				.antMatchers("/views/rrhh/persona.jsf").hasAnyAuthority("ADMI")
				.antMatchers("/views/rrhh/nomina/cargo.jsf")
				.hasAnyAuthority("ADMI")
				.antMatchers("/views/rrhh/nomina/empleado.jsf")
				.hasAnyAuthority("ADMI")

				.antMatchers("/views/configuracion/parametro.jsf")
				.hasAnyAuthority("ADMI")
				.antMatchers("/views/seguridad/usuario/bitacora.jsf")
				.hasAnyAuthority("SEGU")

				.antMatchers("/views/views/seguridad/cambiarClave.jsf")
				.access("isAuthenticated()").and().formLogin()
				.loginPage("/views/seguridad/login.jsf")
				.defaultSuccessUrl("/templates/layout/masterLayout.jsf").and().logout()
				.logoutUrl("/views/home.jsf")
				.logoutSuccessUrl("/views/seguridad/login.jsf")
				.invalidateHttpSession(true).deleteCookies("JSESSIONID").and()
				.sessionManagement().invalidSessionUrl("/index.html")
				.maximumSessions(1);
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth)
			throws Exception {
		PersistenceConfig persistenceConfig = new PersistenceConfig();

		auth.jdbcAuthentication().dataSource(persistenceConfig.dataSource())
				.passwordEncoder(new ShaPasswordEncoder(256))
				.usersByUsernameQuery(getUserQuery())
				.authoritiesByUsernameQuery(getAuthoritiesQuery());
	}

	private String getAuthoritiesQuery() {
		return "select p.cedula , r.nombre "
				+ "from negosys.persona as p, negosys.rol as r, negosys.rolusuario as ur "
				+ "where p.personaid = ur.personaid and r.rolid = ur.rolid and ur.activo=true and p.cedula = ?";
	}

	private String getUserQuery() {
		return "select cedula, password, activo from negosys.persona "
				+ "where cedula = ?";
	}
}
