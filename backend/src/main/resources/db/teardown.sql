SET REFERENTIAL_INTEGRITY FALSE; -- 모든 제약조건 비활성화
truncate table TODO_TB;
truncate table USER_TB;
SET REFERENTIAL_INTEGRITY TRUE; -- 모든 제약조건 활성화