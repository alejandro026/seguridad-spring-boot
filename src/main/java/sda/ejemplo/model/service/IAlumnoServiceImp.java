package sda.ejemplo.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sda.ejemplo.model.dao.IAlumnoDao;
import sda.ejemplo.model.entity.Alumno;

@Service
public class IAlumnoServiceImp implements IAlumnoService{
	
	@Autowired
	private IAlumnoDao alumnoDao;

	@Override
	@Transactional(readOnly= true)
	public List<Alumno> findAll() {		
		return alumnoDao.findAll();
	}


	@Override
	public Alumno save(Alumno alumno) {
		// TODO Auto-generated method stub
		return alumnoDao.save(alumno);
	}

	@Override
	@Transactional(readOnly= true)
	public Alumno findById(Integer id) {
		// TODO Auto-generated method stub
		return alumnoDao.findById(id).orElse(null);
	}


	@Override
	public void delete(Integer id) {
		// TODO Auto-generated method stub
		alumnoDao.deleteById(id);
	}


}
