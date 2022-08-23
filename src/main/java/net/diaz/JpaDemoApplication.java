package net.diaz;
// Ver si esta data.sql o Data.util
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.w3c.dom.ls.LSOutput;

import net.diaz.model.Categoria;
import net.diaz.model.Perfil;
import net.diaz.model.Usuario;
import net.diaz.model.Vacante;
import net.diaz.repository.CategoriasRepository;
import net.diaz.repository.PerfilesRepository;
import net.diaz.repository.UsuariosRepository;
import net.diaz.repository.VacantesRepository;

@SpringBootApplication
public class JpaDemoApplication implements CommandLineRunner{
	
	@Autowired
	private CategoriasRepository repoCategorias;
	
	@Autowired
	private VacantesRepository repoVacantes;

	@Autowired
	private UsuariosRepository repoUsuarios;
	
	@Autowired
	private PerfilesRepository repoPerfiles;
	
	
	public static void main(String[] args) {
		SpringApplication.run(JpaDemoApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		
		// guardar(); // buscarPorId();	// modificar(); 	// eliminar(); 	// eliminarTodos();
		// encontrarPorIds();  // buscarTodas(); 	// existId();	// guardarTodas();
		// buscarTodosOrdenados();	// buscarTodosPaginacion();	// buscarTodosPaginacionOrdenados();
		//contar();	//buscarVacantes(); 	//guardarVacante();	//crearPerfilesAplicacion(); 
		//crearusuarioConDosPerfiles(); // buscarUsuario(); // buscarVacantesPorEstatus();
		// buscarVacantesPorDestacadoEstatus(); 
		// buscarVacanteSalario();
		buscarVacantesVariosEstatus();
	}
	
	// Buscar vacantes por varios estatus (In)
	private void buscarVacantesVariosEstatus() {
		String[] estatus = new String[] {"Eliminada", "Aprobada"};
		List<Vacante> lista = repoVacantes.findByEstatusIn(estatus);
		System.out.println("La cantidad de registos encontrados es: " + lista.size());
		for(Vacante v : lista) {
			System.out.println(v.getId() + " " + v.getNombre() + ": $" + v.getEstatus());
		}
	}
	
	// Buscar vacantes por rango de salario en forma descendente (Between)
	private void buscarVacanteSalario() {
		List<Vacante> lista = repoVacantes.findBySalarioBetweenOrderBySalarioDesc(7000, 14000);
		System.out.println("Cantidad de registros encontrados: " + lista.size());
		for(Vacante v : lista) {
			System.out.println(v.getId() + " " + v.getNombre() + ": $" + v.getSalario());
		}
	}
	
	// Buscar vacante por Destacado y Estatus ordenado por Id Desc
	private void buscarVacantesPorDestacadoEstatus() {
		List<Vacante> lista = repoVacantes.findByDestacadoAndEstatusOrderByIdDesc(1, "Aprobada");
		System.out.println("Cantidad de registros encontrados: " + lista.size());
		for(Vacante v : lista) {
			System.out.println(v.getId() + " " + v.getNombre() + " " + v.getEstatus() + " " + v.getDestacado());
		}
	}
	
	// Metodo para buscar vacantes por estatus
	private void buscarVacantesPorEstatus() {
		List<Vacante> lista = repoVacantes.findByEstatus("Aprobada");
		System.out.println("Cantidad de registros encontrados: " + lista.size());
		for(Vacante v : lista) {
			System.out.println(v.getId() + " " + v.getNombre() + " " + v.getEstatus());
		}
	}
	// Metodo para buscar un usuario y mostrar sus perfiles asociados
	private void buscarUsuario() {
		Optional<Usuario> optional = repoUsuarios.findById(1);
		if(optional.isPresent()) {
			Usuario u = optional.get();
			System.out.println("Usuario: " + u.getNombre());
			System.out.println("Perfiles asignados: ");
			for(Perfil p : u.getPerfiles()) {
				System.out.println(p.getPerfil());
			}
		}else {
			System.out.println("Usuario no encontrado");
		}
	}
	
	// Crear un usuario con dos perfiles ADMINISTRADOR / SUPERVISOR
	private void crearusuarioConDosPerfiles() {
		Usuario usu = new Usuario();
		usu.setNombre("Martin Diaz");
		usu.setEmail("luzuriagajuliomartin@gmail.com");
		usu.setFechaRegistro(new Date());
		usu.setUsername("Diaz");
		usu.setPassword("1234");
		usu.setEstatus(1);
		
		Perfil p1 = new Perfil();
		p1.setId(2);
		
		Perfil p2 = new Perfil();
		p2.setId(3);
		
		usu.agregar(p1);
		usu.agregar(p2);
		
		repoUsuarios.save(usu);
	}
	
	/*
	 *  Metodo para crear PERFILES / ROLES
	 */
	private void crearPerfilesAplicacion() {
		repoPerfiles.saveAll(getPerfilesAplicacion());
	}
	
	private void buscarVacantes(){
		List<Vacante> lista = repoVacantes.findAll();
		for(Vacante v : lista) {
			System.out.println(v.getId() + " " + v.getNombre() + "-> " + v.getCategoria().getNombre());
		}
	}
	
	private void guardarVacante() {
		Vacante vacante = new Vacante();
		vacante.setNombre("Profesor de Matemáticas");
		vacante.setDescripcion("Escuela primaria solicita profesor para curso de Matemática");
		vacante.setFecha(new Date());
		vacante.setSalario(8500);
		vacante.setEstatus("Aprobada");
		vacante.setDestacado(0);
		vacante.setImagen("escuela.png");
		vacante.setDetalles("<h1>Los requisitos para profesor de matemáticas </h1>");
		Categoria cat = new Categoria();
		cat.setId(15); // Con colocar solo el Id se va a concretar la relacion
		vacante.setCategoria(cat);
		repoVacantes.save(vacante);
	}
	
	//************************************************************************************
	
// Buscar todos con paginacion y ordenados
	private void buscarTodosPaginacionOrdenados() {
		
		Page<Categoria> page = repoCategorias.findAll(PageRequest.of(0, 5, Sort.by("nombre"))); // El cero es la 1er pagina y el 5 la cant de regs/pagina
		System.out.println("Total de registros: " + page.getTotalElements());  // Total de registros de la tabla
		System.out.println("Total de paginas: " + page.getTotalPages());  // Total de registros de la tabla
		for(Categoria c : page.getContent()) {
			System.out.println(c.getId() + " " + c.getNombre());
		}
	}
	
///  Buscar todos con Paginacion
	private void buscarTodosPaginacion() {
		
		Page<Categoria> page = repoCategorias.findAll(PageRequest.of(0, 5)); // El cero es la 1er pagina y el 5 la cant de regs/pagina
		System.out.println("Total de registros: " + page.getTotalElements());  // Total de registros de la tabla
		System.out.println("Total de paginas: " + page.getTotalPages());  // Total de registros de la tabla
		for(Categoria c : page.getContent()) {
			System.out.println(c.getId() + " " + c.getNombre());
		}
	}
	
//  Buscar todos ordenados por un campo
	private void buscarTodosOrdenados() {
//    por defecto ordena por Id que es la PK
		List<Categoria> categorias = repoCategorias.findAll(Sort.by("nombre").descending()); // mismo nombre de la clase de modelo
		for(Categoria cat : categorias) {
			System.out.println(cat.getId() + " " + cat.getNombre());
		}
	}
	
	private void borrarTodosEnBloque() {
// Metodo de JpaRepostory
		repoCategorias.deleteAllInBatch();   
	}
	
	// Metod saveAll
	private void guardarTodas() {
		List<Categoria> categorias = getListaCategorias();
		repoCategorias.saveAll(categorias);
	}
	
	private void existId() {
		boolean existe = repoCategorias.existsById(25);
		System.out.println("La categoria existe: " + existe);
	}
	
	private void buscarTodas() {
//		Iterable<Categoria> categorias = repo.findAll();
//  Implementando Jpa devuelve un Objeto tipo List
		List<Categoria> categorias = repoCategorias.findAll();
		for(Categoria cat : categorias) {
			System.out.println(cat.getId() + " " + cat.getNombre());
		}
	}
	
	private void encontrarPorIds() {
		List<Integer> ids = new LinkedList<Integer>();
		ids.add(1);
		ids.add(4);
		ids.add(10);
		Iterable<Categoria> categorias = repoCategorias.findAllById(ids);
		for(Categoria cat : categorias) {
			System.out.println(cat);
		}
	}
	
	private void eliminarTodos() {
		repoCategorias.deleteAll();
	}
	
	private void contar() {
		Long count = repoCategorias.count();
		System.out.println("Cantidad de categorias: "+ count);
	}
	
	private void eliminar() {
		int idCategoria = 1;
		repoCategorias.deleteById(idCategoria);
	}
	
	private void modificar() {
		Optional<Categoria> optional = repoCategorias.findById(2);
		if(optional.isPresent()){
			Categoria catTmp = optional.get();
			catTmp.setNombre("Ing de software");
			catTmp.setDescripcion("Empleos relacionados a desarrollo de sistemas");
			repoCategorias.save(catTmp);
			
			System.out.println(optional.get());
		}else {
			System.out.println("Categoria no encontrada");
		}
	}
	
	private void buscarPorId() {
		Optional<Categoria> optional = repoCategorias.findById(1);
		if(optional.isPresent()){
			System.out.println(optional.get());
		}else {
			System.out.println("Categoria no encontrada");
		}
	}
	
	private void guardar() {
		Categoria cat = new Categoria();
		cat.setNombre("Finanzas");
		cat.setDescripcion("Empleos relacionados con finanzas y contabilidad");
		repoCategorias.save(cat);
		System.out.println(cat);
	}
	
	private List<Categoria> getListaCategorias(){
		List<Categoria> lista = new LinkedList<Categoria>();
		
		// Categoria 1
		Categoria cat1 = new Categoria();
		cat1.setNombre("Programador de Blockchain");
		cat1.setNombre("Empleos relacionados con Bitcoin y otras criptomonedas");
		
		// Categoria 2
		Categoria cat2 = new Categoria();
		cat2.setNombre("Soldador/Pintura");
		cat2.setNombre("Empleos relacionados con soldadura y pintura");
		
		// Categoria 3
		Categoria cat3 = new Categoria();
		cat3.setNombre("Ingeniero Industrial");
		cat3.setNombre("Empleos relacionados con la ingenieria industrial");
		
		lista.add(cat1);
		lista.add(cat2);
		lista.add(cat3);
		
		return lista;
	}
	
	/*
	 * Metodo que regresa una lista de perfiles
	 */
	
	private List<Perfil>getPerfilesAplicacion(){
		List<Perfil> lista = new LinkedList<Perfil>();
		
		Perfil p1 = new Perfil();
		p1.setPerfil("SUPERVISOR");
		
		Perfil p2 = new Perfil();
		p2.setPerfil("ADMINSTRADOR");
		
		Perfil p3 = new Perfil();
		p3.setPerfil("USUARIO");
		
		lista.add(p1);
		lista.add(p2);
		lista.add(p3);
		
		return lista;
	}
}
