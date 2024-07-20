package com.example.demo.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.demo.entity.Course;
import com.example.demo.exception.CourseNotFoundException;
import com.example.demo.repo.CourseRepository;
import com.example.demo.service.CourseService;

@Service
public class CourseServiceImpl implements CourseService {

	//@Autowired
//	private CourseRepository courseRepository;



	@Value("${aws.s3.bucket.name}")
	private String bucketName;

	@Value("${aws.access.key.id}")
	private String accessKeyId;

	@Value("${aws.secret.acccess.key}")
	private String secretAccessKey;

	@Value("${aws.region}")
	private String region;
	
	private final CourseRepository courseRepository;

	public CourseServiceImpl(CourseRepository courseRepository) {
		this.courseRepository = courseRepository;
	}

	private AmazonS3 initializeAmazonS3Client() {
		BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKeyId, secretAccessKey);
		return AmazonS3ClientBuilder.standard().withRegion(region)
				.withCredentials(new AWSStaticCredentialsProvider(awsCredentials)).build();
	}

	@Override
	public Course saveCourse(Course course, MultipartFile image) {
		String imageUrl = uploadImageToS3(image);
		course.setCourseImageUrl(imageUrl);
		return courseRepository.save(course);
	}

	private String uploadImageToS3(MultipartFile image) {
		AmazonS3 s3Client = initializeAmazonS3Client();
		String imageFileName = UUID.randomUUID().toString() + " _" + image.getOriginalFilename();
		File file = convertMultiPartToFile(image);
		s3Client.putObject(new PutObjectRequest(bucketName, imageFileName, file));
		file.delete();
		return s3Client.getUrl(bucketName, imageFileName).toString();
	}

	private File convertMultiPartToFile(MultipartFile file) {
		File convFile = new File(file.getOriginalFilename());
		try (FileOutputStream fos = new FileOutputStream(convFile)) {
			fos.write(file.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return convFile;
	}

	@Override
	public List<Course> getAllCourses() {
		return courseRepository.findAll();
	}

	@Override
	public Course getCourseById(Long courseId) {
		return courseRepository.findById(courseId)
				.orElseThrow(() -> new CourseNotFoundException("Course not found with id: " + courseId));

	}

	@Override
	public void deleteCourse(Long courseId) {
		if (!courseRepository.existsById(courseId)) {
			throw new CourseNotFoundException("Course not found with id: " + courseId);
		}

		courseRepository.deleteById(courseId);
		System.out.println("Course Id Deleted: " + courseId);
	}

}
