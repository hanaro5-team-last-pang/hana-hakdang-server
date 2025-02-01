--- 태그 목록
--- 추후 추가 및 수정 가능
INSERT INTO TAG (id, tag_name)
VALUES (1, '정기예금'),
       (2, '외화상품'),
       (3, '신탁'),
       (4, '적금'),
       (5, '대출');


--- 상품 목록
--- 추후 추가 및 수정 가능
INSERT INTO HANA_ITEM (id, tag_id, item_title, item_content, hana_url, created_at, updated_at)
VALUES (1, 1, '3·6·9 정기예금', '3개월마다 중도해지 혜택 제공',
        'https://www.kebhana.com/cont/mall/mall08/mall0801/mall080101/1419598_115126.jsp',
        CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (2, 1, '고단위 플러스(금리연동형)', '이자 지급 방법도 내 맘대로! 이자 지급 시기도 내 맘대로!',
        'https://www.kebhana.com/cont/mall/mall08/mall0801/mall080101/1419601_115126.jsp',
        CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (3, 1, '하나의 정기예금', '계약기간, 가입금액 자유롭고 자동재예치를 통해 자금관리',
        'https://www.kebhana.com/cont/mall/mall08/mall0801/mall080101/1479088_115126.jsp',
        CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (4, 1, '행복knowhow 연금예금', '노후자금, 생활자금, 재투자자금으로 설계 가능',
        'https://www.kebhana.com/cont/mall/mall08/mall0801/mall080101/1419664_115126.jsp',
        CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (5, 1, '정기예금', '목돈을 일정기간 예치하여 안정적인 수익을 추구',
        'https://www.kebhana.com/cont/mall/mall08/mall0801/mall080101/1419602_115126.jsp',
        CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (6, 1, '양도성예금증서(CD)', '만기일 이전 양도가 가능하여 단기자금 운용에 적합',
        'https://www.kebhana.com/cont/mall/mall08/mall0801/mall080101/1419604_115126.jsp',
        CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (7, 1, '1년 연동형 정기예금', '서울보증보험의 보증서 발급 담보용',
        'https://www.kebhana.com/cont/mall/mall08/mall0801/mall080101/1419635_115126.jsp',
        CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

       (8, 2, '하나 밀리언달러 통장', '해외주식 매매가 가능한 외화 다통화 입출금 통장.',
        'https://www.kebhana.com/cont/mall/mall08/mall0801/mall080103/1475034_115188.jsp',
        CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (9, 2, '하나 빌리언달러 통장', '법인 대상 수출입 수수료 우대 혜택 통장.',
        'https://www.kebhana.com/cont/mall/mall08/mall0801/mall080103/1481500_115188.jsp',
        CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (10, 2, '일달러 외화적금', '$1부터 자유롭게 적립하고 일부 인출도 가능한 미달러 전용 외화 상품.',
        'https://www.kebhana.com/cont/mall/mall08/mall0801/mall080102/1469492_115157.jsp',
        CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (11, 2, '더 와이드(The Wide)외화적금', '환율 및 외환 수수료 등의 우대 혜택을 제공하며 분할 인출이 가능한 외화 상품.',
        'https://www.kebhana.com/cont/mall/mall08/mall0801/mall080102/1431721_115157.jsp',
        CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (12, 2, '스마트팝콘 외화적립예금', '가입기간 중 송금, 환전 등 외환거래 발생 시 우대이율을 추가로 제공하는 상품.',
        'https://www.kebhana.com/cont/mall/mall08/mall0801/mall080101/1431720_115126.jsp',
        CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

       (13, 3, '유언대용신탁 하나 리빙트러스트', '1:1 맞춤계약, Care Trust, 증여신탁, 부동산관리 신탁 등 다양한 유언대용 서비스 제공.',
        'https://m.kebhana.com/cont/hidden/livingtrust/index.html', CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP),
       (14, 3, '특정금전신탁', '운용지시서를 기반으로 고객의 지시에 따라 자금을 단독 운용하는 금전신탁.',
        'https://www.kebhana.com/cont/mall/mall08/mall0801/mall080104/1420129_115190.jsp',
        CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (15, 3, '공익신탁', '공익사업(학문, 기술, 문화, 청소년 육성 등)을 목적으로 설정된 신탁.',
        'https://www.kebhana.com/cont/mall/mall08/mall0801/mall080104/1420130_115190.jsp',
        CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

       (16, 4, '(내맘)적금', '저축금액, 만기일, 자동이체 구간까지 내 맘대로 디자인하는 DIY 적금.',
        'https://www.kebhana.com/cont/mall/mall08/mall0801/mall080102/1461831_115157.jsp',
        CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (17, 4, '연금하나 월복리 적금', '연금 가입자에게 월복리 혜택 제공',
        'https://www.kebhana.com/cont/mall/mall08/mall0801/mall080102/1455928_115157.jsp',
        CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (18, 4, '트래블로그 여행 적금', '트래블로그 카드 사용 실적으로 우대금리 제공',
        'https://www.kebhana.com/cont/mall/mall08/mall0801/mall080102/1495010_115157.jsp',
        CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),


       (19, 5, '하나 전세금안심대출', '최대 4억원 (단, 부부합산 1주택자는 최대 2억원)',
        'https://www.kebhana.com/cont/mall/mall08/mall0802/mall080201/1420287_115194.jsp?_menuNo=98786',
        CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (20, 5, '하나원큐 주택담보대출', '신청부터 실행까지 방문없이 모바일로 OK',
        'https://www.kebhana.com/cont/mall/mall08/mall0802/mall080202/1481336_115196.jsp?_menuNo=98786',
        CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (21, 5, '하나주거래대출', '최저 3백만원 ~ 최대 5천만원(단, 10만원 단위로 신청가능)',
        'https://www.kebhana.com/cont/mall/mall08/mall0802/mall080204/1474883_115200.jsp?_menuNo=98786',
        CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);



