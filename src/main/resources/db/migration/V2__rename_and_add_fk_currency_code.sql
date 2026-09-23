-- 1. 컬럼명 변경
ALTER TABLE financial_product
    RENAME COLUMN currency to currency_code;

-- 2. NOT_NULL 제약 조건 설정
ALTER TABLE financial_product
    ALTER COLUMN currency_code SET NOT NULL;

-- 3. 외래키 제약 조건 추가
-- (exchange_rate 테이블의 PK인 currency_code 컬럼을 참조)
ALTER TABLE financial_product
    ADD CONSTRAINT fk_financial_product_exchange_rate
        FOREIGN KEY (currency_code)
            REFERENCES exchange_rate (currency_code);



