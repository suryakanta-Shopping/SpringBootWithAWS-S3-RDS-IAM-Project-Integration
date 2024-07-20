package com.example.demo.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entity.Course;
import com.example.demo.service.CourseService;

@RestController
@RequestMapping("/course")
public class CourseController {

	@Autowired
	private CourseService courseService;

	// createCourse
	   @PostMapping("/create") 
	   public ResponseEntity<Course>createCourse(
	    @RequestParam("courseName") String courseName,
	   
	   @RequestParam("courseDuration") String courseDuration,
	   
	   @RequestParam("coursePrice") Double coursePrice,
	   
	   @RequestParam("image") MultipartFile image) {
	   
	   Course course=new Course(); course.setCourseName(courseName);
	   course.setCourseDuration(courseDuration); course.setCoursePrice(coursePrice);
	   return ResponseEntity.ok(courseService.saveCourse(course,image)); }
	 
	/*
	 * public ResponseEntity<Course> createCourse(@RequestBody Course course) { try
	 * { Course course2 = courseService.saveCourse( course2.getCourseName(),
	 * course2.getCourseDuration(), course2.getCoursePrice(),
	 * course2.getCourseImageUrl()); return new ResponseEntity<>(course,
	 * HttpStatus.CREATED); } catch (IOException e) { return new
	 * ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR); } }
	 */

	// getAllCourse
	@GetMapping("/allcourse")
	public ResponseEntity<List<Course>> getAllCourse() {
		return ResponseEntity.ok(courseService.getAllCourses());
	}

	// getCourseById
	@GetMapping("/{courseId}")
	public ResponseEntity<Course> getCourseById(@PathVariable Long courseId) {
		return ResponseEntity.ok(courseService.getCourseById(courseId));
	}

	// deleteCourse
	@DeleteMapping("/{courseId}")
	public ResponseEntity<Course> deleteCourse(@PathVariable Long courseId) {
		courseService.deleteCourse(courseId);
		return ResponseEntity.noContent().build();
	}
}
