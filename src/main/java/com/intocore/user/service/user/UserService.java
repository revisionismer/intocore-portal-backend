package com.intocore.user.service.user;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import com.intocore.handler.exception.CustomApiException;
import com.intocore.user.domain.User;
import com.intocore.user.domain.UserRepository;
import com.intocore.user.web.dto.user.UserInfoRespDto;
import com.intocore.user.web.dto.user.UserProfileRespDto;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(rollbackFor = Exception.class)  // RuntimeException 말고도 모든 예외가 터졌을시 롤백시킨다.
@RequiredArgsConstructor
public class UserService {
	
	private final UserRepository userRepository;
	
	@Value("${thumnail.path}")
	private String thumnailFolder;
	
	public UserInfoRespDto userInfoByUserId(Long userId) {
		
		Optional<User> userOp = userRepository.findById(userId);
		
		if(userOp.isPresent()) {
			User user = userOp.get();
			
			return new UserInfoRespDto(user);
		} else { 
			throw new CustomApiException("해당 유저가 존재하지 않습니다.");
		}
	}
	
	// 2026-03-28 : 사용자 프로필 사진 업로드 기능 추가
	public UserProfileRespDto userProfilePictureUpdate(Long principalId, MultipartFile profileImageFile) {
		// 3-1. 파일명 + 확장자 가져오기
		String originalFileName = profileImageFile.getOriginalFilename();
				
		// 3-2. 확장자 추출
		String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
				
		// 3-3. UUID[Universally Unique IDentifier] : 네트워크 상에서 고유성이 보장되는 id를 만들기 위한 표준 규약. 범용고유식별자.
		UUID uuid = UUID.randomUUID();
				
		// 3-4. UUID + 확장자 명으로 저장
		String imageFileName = uuid.toString() + extension;
		
		// 3-5. 3-4에서 가져온 실제 파일이  저장될 경로에 3-3에서 만든 UUID 파일명을 더해서 Path를 만든다.
		Path imageFilePath = Paths.get(thumnailFolder + imageFileName);
		
		System.out.println(imageFilePath);
		
		// 3-6. 파일을 실제 저장 경로에 저장하는 로직 -> 통신 or I/O -> 예외가 발생할 수 있다(try-catch로 묶어서 처리)
		try {
			
			Files.write(imageFilePath, profileImageFile.getBytes());
			
		} catch (Exception e) {
			throw new MaxUploadSizeExceededException(1000);
		}
		
		// 3-7. 로그인 유저 정보를 가져온다.
		User userEntity = userRepository.findById(principalId).orElseThrow(() -> {
			throw new CustomApiException("해당 유저를 찾을 수 없습니다.");
		});		
			
		// 3-8. 변경감지를 이용해 set을 호출하면 업데이트
		userEntity.setProfileImageUrl(imageFileName);
		
		return new UserProfileRespDto(userEntity);
				
	}
}
