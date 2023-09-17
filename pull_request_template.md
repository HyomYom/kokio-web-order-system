## 변경사항
- 판매자 상품/세부상품 -> 등록 업데이트까지 [ 조회 및 삭제 추후 예정]
- 세부적인 모듈화 작업 설정(모듈별 DataSourceConfig / JwtFilter / Exception)

## AS-IS
- 각각의 모듈마다 설정 패키지를 작성
- 다른 모듈에서 유저 모듈로부터 유저 데이터를 불러올 때 JSON 직렬화 문제

## TO-BE
- DataBase의 확실 한 분리를 위한 각 모듈별 EntityManager 설정을 분리함
- 공통되는 설정은 모듈은 분리해 작성 후 각 하위 모듈이 그것이 상속 받게함
- Security Filter 내에서 발생한 오류는 기존 ExceptionHandler로 잡히지 않아, 필터 내 CustomHandler를 만들어 적용

## 테스트