package com.demo.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.demo.entites.Activity;
import com.demo.entites.Address;
import com.demo.entites.Education;
import com.demo.entites.Jobseeker;
import com.demo.repository.JobseekerRepository;

import jakarta.persistence.EntityManager;
@Service
public class JobseekerServiceImpl implements JobseekerService {
	
	@Autowired
	private JobseekerRepository jobseekerRepository;
	
	@Autowired
	private ActivityService activityService;
	
	@Autowired
	private EntityManager entityManager;

	@Override
	public List<Jobseeker> getAllJobseekers() {
		List<Jobseeker> jobseekerList = jobseekerRepository.findAll();
		return jobseekerList;
	}

	@Override
	 public ResponseEntity<Jobseeker> getJobseekerById(long id) {
        Optional<Jobseeker> jobseeker = jobseekerRepository.findById(id);
        
        if (jobseeker.isPresent()) {
            return new ResponseEntity<>(jobseeker.get(), HttpStatus.OK);  // Return jobseeker if found with HTTP 200 OK
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);  // Return 404 if not found
        }
    }

	@Override
	public Jobseeker addJobseeker(Jobseeker jobseeker) {
		Address address = jobseeker.getAddress();
		address = entityManager.merge(address);
		jobseeker.setAddress(address);
		List<Education> educations = jobseeker.getEducation();
		for(int i=0;i<=educations.size();i++)
		{
			Education education = educations.get(i);
			 education = entityManager.merge(education);
             educations.set(i, education);
		}
		
		jobseeker.setEducation(educations);
		jobseeker = entityManager.merge(jobseeker);
		Jobseeker jobseeker2 = jobseekerRepository.save(jobseeker);
		Activity activity = new Activity();
		activity.setActivityDate(new Date());
		activity.setActivityType("jobseeker added");
		activity.setEmployerId(0);
		activity.setJobPostId(0);
		activity.setJobSeekerId(jobseeker2.getJobSeekerId());
		activity.setDetails("through Jobseeker registration from");
		activityService.addActivity(activity);
		return jobseeker2;
	}

	@Override
	public ResponseEntity<String> deleteJobseeker(long id) {
		Activity activity = new Activity();
		activity.setActivityDate(new Date());
		activity.setActivityType("jobseeker deleted");
		activity.setEmployerId(0);
		activity.setJobPostId(0);
		activity.setJobSeekerId(id);
		activity.setDetails("through admin");
		activityService.addActivity(activity);
		
        Optional<Jobseeker> jobseeker = jobseekerRepository.findById(id);
        
        if (jobseeker.isPresent()) {
            jobseekerRepository.deleteById(id);
            return new ResponseEntity<>("Jobseeker deleted successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Jobseeker not found with given id", HttpStatus.NOT_FOUND);
        }
    }

	@Override
	public Jobseeker updateJobseeker(Jobseeker jobseeker) {
		Jobseeker jobseeker2 = jobseekerRepository.save(jobseeker);
		Activity activity = new Activity();
		activity.setActivityDate(new Date());
		activity.setActivityType("jobseeker updated");
		activity.setEmployerId(0);
		activity.setJobPostId(0);
		activity.setJobSeekerId(jobseeker2.getJobSeekerId());
		activity.setDetails("through Jobseeker registration from");
		activityService.addActivity(activity);
		return jobseeker2;
	}

	@Override
	public List<Jobseeker> getJobseekersByQualification(String degree) {
		return jobseekerRepository.getJobseekersByQualification(degree);
	}

	@Override
	public List<Jobseeker> getJobseekerByExperience(int experience) {
		return jobseekerRepository.getJobseekerByExperience(experience);
	}

}
