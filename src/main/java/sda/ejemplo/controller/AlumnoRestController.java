package sda.ejemplo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.hibernate.exception.DataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import sda.ejemplo.model.entity.Alumno;
import sda.ejemplo.model.service.IAlumnoService;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class AlumnoRestController {

	@Autowired 
	public IAlumnoService alumnoService;
	
	@GetMapping("/alumnos")
	public List<Alumno> index(){
		return alumnoService.findAll();
	}
	
	@GetMapping("/alumnos/{id}")
	public Alumno listar(@PathVariable Integer id) {
		return alumnoService.findById(id);
	}
	
	@PostMapping("/alumnos")
	public ResponseEntity<?> guardar(@Valid @RequestBody Alumno alumno, BindingResult resultado) {
		Map<String, Object> mapa= new HashMap<String, Object>();
		Alumno alumnoNuevo= null;
		
		if(resultado.hasErrors()) {
			List<String> errores= resultado.getFieldErrors().stream()
					.map(e->"El campo "+e.getField() +" "+ e.getDefaultMessage()).collect(Collectors.toList());
			mapa.put("errors", errores);
			return ResponseEntity.badRequest().body(mapa);
		}
		
		try {
			alumnoNuevo= alumnoService.save(alumno);
		} catch (DataAccessException e2) {
			mapa.put("mensaje", "Fallo al guardar el alumno");
			if(e2.getRootCause().toString().contains("Duplicate entry")) {
				mapa.put("error", "El correo electrónico proporcionado ya está registrado. Por favor, verifica.");

			}else {
				mapa.put("error", e2.getMostSpecificCause()+" : "+ e2.getMessage());
			}
			return ResponseEntity.internalServerError().body(mapa);
			
		}
		
		mapa.put("mensaje", "El alumno se guardo con exito");
		mapa.put("alumno", alumnoNuevo);
		
		return ResponseEntity.ok(mapa);

	}
	
	@PutMapping("/alumnos/{id}")
	public ResponseEntity<?> actualizar(@Valid @RequestBody Alumno alumno, BindingResult resultado, @PathVariable Integer id) {
		Map<String, Object> mapa= new HashMap<String, Object>();
		Alumno alumnoActual= alumnoService.findById(id);
		alumnoActual.setNombre(alumno.getNombre());
		alumnoActual.setNumeroControl(alumno.getNumeroControl());
		alumnoActual.setEmail(alumno.getEmail());
		
		if(resultado.hasErrors()) {
			List<String> errores= resultado.getFieldErrors().stream()
					.map(e->"El campo "+e.getField() +" "+ e.getDefaultMessage()).collect(Collectors.toList());
			mapa.put("errors", errores);
			return ResponseEntity.badRequest().body(mapa);
		}
		
		try {
			alumno= alumnoService.save(alumnoActual);
		} catch (DataAccessException e2) {
			
			mapa.put("mensaje", "Fallo al actualizar el alumno");
			mapa.put("error", e2.getMostSpecificCause()+" : "+ e2.getMessage());

			return ResponseEntity.internalServerError().body(mapa);
			
		}
		
		mapa.put("mensaje", "El alumno se actualizo con exito");
		mapa.put("alumno", alumnoActual);
		
		return ResponseEntity.ok(mapa);

		
//		return alumnoService.save(alumnoActual);
	}
	
	@DeleteMapping("/alumnos/{id}")
	public void eliminar(@PathVariable Integer id) {
		alumnoService.delete(id);
	}
}
