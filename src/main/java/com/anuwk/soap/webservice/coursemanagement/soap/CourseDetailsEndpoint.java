package com.anuwk.soap.webservice.coursemanagement.soap;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import com.anuwk.soap.webservice.coursemanagement.bean.Course;
import com.anuwk.soap.webservice.coursemanagement.exception.CourseNotFoundException;
import com.anuwk.soap.webservice.coursemanagement.soap.service.CourseDetailsService;
import com.anuwk.soap.webservice.coursemanagement.soap.service.CourseDetailsService.Status;

import xyz.un4ckn0wl3z.CourseDetails;
import xyz.un4ckn0wl3z.DeleteCourseDetailsRequest;
import xyz.un4ckn0wl3z.DeleteCourseDetailsResponse;
import xyz.un4ckn0wl3z.GetAllCourseDetailsRequest;
import xyz.un4ckn0wl3z.GetAllCourseDetailsResponse;
import xyz.un4ckn0wl3z.GetCourseDetailsRequest;
import xyz.un4ckn0wl3z.GetCourseDetailsResponse;

@Endpoint
public class CourseDetailsEndpoint {
	
	@Autowired
	CourseDetailsService service;
	

	@PayloadRoot(namespace="http://www.un4ckn0wl3z.xyz",localPart="GetCourseDetailsRequest")
	@ResponsePayload
	public GetCourseDetailsResponse processCourseDetailsRequest(@RequestPayload GetCourseDetailsRequest request) {
		
		Course course =  service.findById(request.getId());
		
		if(course==null) {
			throw new CourseNotFoundException("Invalid Course Id " + request.getId());
		}
		
		return mapCourseDetails(course);
	}


	private GetCourseDetailsResponse mapCourseDetails(Course course) {
		GetCourseDetailsResponse response = new GetCourseDetailsResponse();
		response.setCourseDetails(mapCourse(course));
		return response;
	}
	
	private GetAllCourseDetailsResponse mapAllCourseDetails(List<Course> courses) {
		GetAllCourseDetailsResponse response = new GetAllCourseDetailsResponse();
		for (Course course : courses) {
			CourseDetails mapCourseDetails = mapCourse(course);
			response.getCourseDetails().add(mapCourseDetails);
		}
		return response;
	}


	private CourseDetails mapCourse(Course course) {
		CourseDetails courseDetails = new CourseDetails();
		courseDetails.setId(course.getId());
		courseDetails.setName(course.getName());
		courseDetails.setDescription(course.getDescription());
		return courseDetails;
	}
	
	
	@PayloadRoot(namespace="http://www.un4ckn0wl3z.xyz",localPart="GetAllCourseDetailsRequest")
	@ResponsePayload
	public GetAllCourseDetailsResponse processAllCourseDetailsRequest(@RequestPayload GetAllCourseDetailsRequest request) {
		
		List<Course> course =  service.findAll();
		return mapAllCourseDetails(course);
	}
	
	@PayloadRoot(namespace="http://www.un4ckn0wl3z.xyz",localPart="DeleteCourseDetailsRequest")
	@ResponsePayload
	public DeleteCourseDetailsResponse deleteCourseDetailsRequest(@RequestPayload DeleteCourseDetailsRequest request) {
		
		Status status =  service.deleteById(request.getId());
		
		DeleteCourseDetailsResponse response = new DeleteCourseDetailsResponse();
		response.setStatus(mapStatus(status));
		return response;
	}


	private xyz.un4ckn0wl3z.Status mapStatus(Status status) {
		if(status==Status.FAILURE) {
			return xyz.un4ckn0wl3z.Status.FAILURE;
		}
		return xyz.un4ckn0wl3z.Status.SUCCESS;
	}

}
