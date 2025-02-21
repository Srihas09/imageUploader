package com.demo.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.entites.Activity;
import com.demo.entites.Employer;
import com.demo.entites.Jobseeker;
import com.demo.repository.EmployerRepository;

@Service
public class EmployerServiceImpl implements EmployerService {
	
	@Autowired
	private EmployerRepository employerRepository;
	
	@Autowired
	private ActivityService activityService;

	@Override
	public Employer addEmployer(Employer employer) {
		
		Employer employer2 = employerRepository.save(employer);
		Activity activity = new Activity();
		activity.setActivityDate(new Date());
		activity.setActivityType("Employer added");
		activity.setEmployerId(employer2.getEmployerId());
		activity.setJobPostId(0);
		activity.setJobSeekerId(0);
		activity.setDetails("through Employer registration from");
		activityService.addActivity(activity);
		return employerRepository.save(employer);
	}

	@Override
	public List<Employer> getAllEmployers() {
		return employerRepository.findAll();
	}

	@Override
	public Optional<Employer> getEmployerById(long id) {
		Optional<Employer> employer = employerRepository.findById(id);
		if(employer!=null)
		{
			return employer;
		}
		else 
		{
			throw new Error("employer not found with given id");
		}
	}

	@Override
	public Employer updateEmployer(Employer employer) {
		Employer employer2 = employerRepository.save(employer);
		Activity activity = new Activity();
		activity.setActivityDate(new Date());
		activity.setActivityType("Employer updated");
		activity.setEmployerId(employer2.getEmployerId());
		activity.setJobPostId(0);
		activity.setJobSeekerId(0);
		activity.setDetails("through Employer registration from");
		activityService.addActivity(activity);
		return employerRepository.save(employer);
	}

	@Override
	public Optional<Employer> deleteEmployer(long id) {
		Optional<Employer> employer = employerRepository.findById(id);
		if(employer!=null)
		{
			Activity activity = new Activity();
			activity.setActivityDate(new Date());
			activity.setActivityType("Employer deleted");
			activity.setEmployerId(id);
			activity.setJobPostId(0);
			activity.setJobSeekerId(0);
			activity.setDetails("by admin");
			activityService.addActivity(activity);
			employerRepository.deleteById(id);
			return employer;
		}
		else 
		{
			throw new Error("employer not found with given id");
		}
	}

}
