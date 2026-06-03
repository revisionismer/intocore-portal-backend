package com.intocore.handler;

import java.util.HashMap;
import java.util.Map;

import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import com.intocore.common.dto.ResponseDto;
import com.intocore.handler.exception.CustomApiException;
import com.intocore.handler.exception.CustomValidationException;

@RestControllerAdvice // 1-1. @ControllerAdvice + @RestController : 모든 exception을 낚아챈다.
public class CustomExceptionHandler {

	private final Logger log = LoggerFactory.getLogger(getClass());
	
	@Value("${spring.servlet.multipart.max-file-size}")
	private String maxFileSize;
	
	@ExceptionHandler(CustomApiException.class)  // 1-2. CustomApiException이 터지면 여기서 캐치해서 매개변수로 넘겨준다.
	public ResponseEntity<?> apiException(CustomApiException e) {
		log.error(e.getMessage());
		return new ResponseEntity<>(new ResponseDto<>(-1, e.getMessage(), null), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(CustomValidationException.class)  // 2-1. CustomValidationException
	public ResponseEntity<?> validationException(CustomValidationException e) {
		log.error(e.getMessage());
		return new ResponseEntity<>(new ResponseDto<>(-1, e.getMessage(), e.getErrorMap()), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(MaxUploadSizeExceededException.class)
	public ResponseEntity<?> validationMaxUploadSizeExceededException(CustomApiException e) {
		log.error(e.getMessage());
		return new ResponseEntity<>(new ResponseDto<>(-1, maxFileSize + "크기를 초과한 파일입니다.", null), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(SizeLimitExceededException.class)
	public ResponseEntity<?> validationSizeLimitExceededException(CustomApiException e) {
		log.error(e.getMessage());
		return new ResponseEntity<>(new ResponseDto<>(-1, maxFileSize + "크기를 초과한 파일입니다.", null), HttpStatus.BAD_REQUEST);
	}
	
	// 2026-05-14 : MethodArgumentNotValidException : @Valid @Validated 어노테이션으로 검증이 실패시 터지는 예외.(ex. @NotNull, @Size 같은 어노테이션이 DTO에 붙어있을때 검증이 실패하면 해당 예외가 터짐)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<?> validationApiException(MethodArgumentNotValidException e) {
		log.error(e.getMessage());
		
		Map<String, String> errorMap = new HashMap<>();

	    e.getBindingResult().getFieldErrors().forEach(error ->
	        errorMap.put(error.getField(), error.getDefaultMessage())
	    );
		
		return new ResponseEntity<>(new ResponseDto<>(-1, "유효성 검사 실패", errorMap), HttpStatus.BAD_REQUEST);
	}
}
