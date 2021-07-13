package com.standardkim.kanban.controller;

import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequiredArgsConstructor
public class TestController {
	private final UserService userService;

	private final AuthenticationService authenticationService;

	private final JwtTokenProvider jwtTokenProvider;

	private final PasswordEncoder passwordEncoder;

	@PostMapping("/login")
	@ResponseStatus(HttpStatus.OK)
	public String login(@RequestBody @Valid UserDto.LoginUserRequest loginUserRequest, HttpServletResponse response) throws Exception {
		SecurityUser securityUser = null;

		try {
			securityUser = (SecurityUser) userService.loadUserByUsername(loginUserRequest.getLogin());
		}
		catch (UsernameNotFoundException e) {
			throw e;
		}

		if(!passwordEncoder.matches(loginUserRequest.getPassword(), securityUser.getPassword())) {
			throw new Exception("Password not matched");
		}

		String refreshToken = jwtTokenProvider.buildRefreshToken();
		String accessToken = jwtTokenProvider.buildAccessToken(securityUser.getLogin(), securityUser.getName());

		//DB에 refershToken 등록 이미 있으면 교체
		authenticationService.saveRefreshToken(securityUser.getId(), refreshToken);

		//refresh token은 쿠키에 저장
		Cookie refreshTokenCookie = new Cookie("REFRESH_TOKEN", refreshToken);
		refreshTokenCookie.setMaxAge(60);
		refreshTokenCookie.setHttpOnly(true);
		response.addCookie(refreshTokenCookie);

		//access token은 payload로 전송
		return accessToken;
	}

	@GetMapping("/welcome")
	public String welcome() {
		return "Shit";
	}
}
