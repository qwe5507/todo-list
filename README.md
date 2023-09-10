# todo-list
미니 프로젝트: TODO List 서비스

# 프로젝트를 이해하는데 도움이 되는 정보

- Login이 완료 되면, 응답 헤더에 JWT토큰을 리턴하고, 인증이 필요한 API는 JWT토큰을 요청 헤더에 기입해야 합니다.
- Spring Security를 사용한 이유는, 현재는 회원 API에 대한 인가만 필요하지만, 추후 기능이 추가되어 더 많은 권한이 필요 할때, Spring Security의 인가기능을 사용하기 위함입니다.
- 로그인 process는 Form로그인시 동작하는 `UsernamePasswordAuthenticationFilter`의 상속하여 `JwtAuthenticationFilter`라는 커스텀 Filter로 Jwt로그인을 진행합니다.
  - 로그인이 완료되면, 수동으로 `AuthenticationToken`을 세팅하여 인증처리를 완료 합니다.
- security에서 Default Login API `/login` api를 `/auth/login`로 변경하여 구현하였습니다.

- 요청에 대한 validaiton은 `Spring validaiton` 라이브러리를 이용하였습니다.
- 응답 파라미터 중, status에 성공 시 `1`, 실패 시 `-1`을 리턴합니다.
  - 프론트 사이드에서 status로 api의 성공/실패 여부를 편리하게 판단하게 하기 위함입니다.
- 회원가입 페이지 및 부가기능 페이지에서 사용할 비인증 API를 추가하였습니다.

# ERD
![image](https://github.com/qwe5507/todo-list/assets/70142711/0805f0d6-5371-4e7c-8831-e2e6c25babcd)

# Request Format
```
curl --location 'http://localhost:8081/todo' \
--header 'Authorization: Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0b2RvIiwiYWNjb3VudElkIjoidXNlciIsInJvbGUiOiJVU0VSIiwiaWQiOjEsImV4cCI6MTY5NDkzMTE5Mn0.Zv_kcE4KCCF5wcI30HSeF3Zz4q-NKYCa8z_7l4mzF8NtHZeJtTTpOoUg1e3XRVlJ0XutNyA29bNXp6X1Q02Eog' \
--data '{
    "title": "첫번쨰 할일",
    "content": "todo API 만들기"
}'
```
# Response Format
```
{
  "code": 1,
  "msg": "Todo 추가 성공",
  "data": {
    "id": 2,
    "userId": 1,
    "title": "첫번쨰 할일",
    "createdAt": "2023-09-10 15:33:36"
  }
}
```

# API 상세 명세서 
https://documenter.getpostman.com/view/14221099/2s9YC1XEPH
