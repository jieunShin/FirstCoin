# FirstCoin
2015 Capstone Design Project

이 프로젝트는 Bitcoin을 활성화 하기 위한 결제 플랫폼입니다.


1. App

  App 폴더에는 Order Application과 Pos Application이 있습니다.
  일반 소비자가 사용하는 Order Application은 비트코인 결제와 지갑 기능이 포함되어 있습니다.
  결제 기능으로는 원거리 주문과 간편 결제가 있으며,
  지갑 기능으로는 비트코인 잔고 확인 및 충전, 자신의 사용 내역 조회 기능이 있습니다.
  
  상인이 사용하는 Pos Application은 일반 Pos에서 제공하지 않는 비트코인 결제가 가능합니다.
  일반 Pos와 동일하게 메뉴 관리와 매출 확인, 통계 확인이 가능합니다. 
  또한, 원거리 주문을 받고 관리하는 기능이 있습니다.
  
  Pos Application의 로그인은 별도의 계정 발급 과정이 필요하며, 계정 발급을 원하시는 분은 bitpay@naver.com으로 연락주시면 됩니다.
  Order Application은 CoinPlug 계정을 사용하여 로그인을 할 수 있습니다.
  CoinPlug의 login API가 사용되었으며, client ID와 client secret key는 공개할 수 없으므로 코드에 포함되어 있지 않습니다.
  때문에 Order Application은 정상 작동되지 않음을 알려드립니다.
  사용을 원하시는 분은 bitpay@naver.com으로 문의해주시기 바랍니다.


2. Web

  Web 폴더에는 상인이 안드로이드 기기가 아닌 웹페이지에서 자신의 Pos를 관리할 수 있는 웹 페이지 코드가 포함되어 있습니다.
  Pos Application에서 제공하는 기능 외에 .xls 파일로 메뉴를 추출하거나
  자신의 .xls 파일에 저장된 메뉴를 업로드할 수 있습니다.
  또한 통계를 그래프로 확인할 수 있습니다.


3. Server

  Server 폴더에는 데이터베이스 서버에 접근할 때 호출되는 php파일이 업로드되어 있습니다.
  php파일 내부에는 보안상의 이유로 데이터베이스 주소와 아이디, 패스워드가 제외되어 있어 정상작동 되지 않습니다.

  
4. Document

  Document 폴더에는 계획서 발표, 1차 중간발표, 2차 중간발표, 최종 발표 ppt와 보고서, 회의록이 업로드되어 있습니다.
  
  
  
  
  
  
  
  
  
  
  





  
The project is a payment platform to activates the Bitcoin.
  
  
  1. App
  
    The App folder contains Pos Application and Order Application.
    Order Application used by consumers contains the Bitcoin wallet and payment functions.
    There are simple payment and remote orders in payment function,
    wallet function include balance check, charge, and their own history lookup function.


    This merchant Pos Application is possible to use Bitcoin payment that is not provided by the General Pos.
    It is also possible to manage menu and check statistic in the same way as general Pos.
    In addition, it has the function to receive and manage remote orders.
    
    
    Sign of the Pos Application is required a seperate account and if you wnat to make an account, please contact                bitpay@naver.com.
    In case of Order Application, you can login using CoinPlug account.
    This application uses login API of CoinPlug, so client ID and client secret key can not be disclosed adn there are not       included in the code.
    Because of this, Order Application is not working properly.
    If you wish to use, please contact bitpay@naver.com.
    
    
    
  2. Web
  
    Web folder contains web page code tha allows you to manage your own Pos in web page, not in the Android device.
    It is possible to extract the .xls files of menu and upload .xls files that is stored their menu in addition to basic        functions of Pos Application.
    You can also check the statistics as a graph.



  3. Server
  
    Server folder contains a php file that is called when the database server is uploaded.
    Inside of the php files, database address, id, and password are excluded for security reasons.
    So, it is not properly worked.



  4. Document
  
    Document folder contains ppt file, reports, and minutes of plan presentation, the primary middle presentation, the           secondary intermediate presentation, and final presentation. 
    
    

    
  
