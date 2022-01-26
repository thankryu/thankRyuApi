# thankRyuApi

url 접근법
1. Controller @RequestMapping 사용
2. 참조된 Service 메소드 및 서비스명 사용
ex) @RequestMapping : common, 서비스명: commonService, 메소드명 : naverShortUrl
= common/commonService/naverShortUrl 로 호출

config.properties 설정파일
naver.client = "네이버 API아이디"
naver.secret = "네이버 API비밀번호"
file.path = "파일 경로"
