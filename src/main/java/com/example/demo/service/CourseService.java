package com.example.demo.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entity.Course;

public interface CourseService {

	Course saveCourse(Course course, MultipartFile image);

	List<Course> getAllCourses();

	Course getCourseById(Long courseId);

	void deleteCourse(Long courseId);
}
