<?php
header("Content-type:text/html;charset=utf8");
class db{
	static private $_instance;
	static private $_connectSource;
	private function __construct(){		
	}
	static public function getInstance(){
		if(self::$_instance instanceof self){
			self::$_instance = new self();
		}
		return self::$_instance;
	}
	
	public static function connect()
	{
		if(!self::$_connectSource){
			self::$_connectSource = mysqli_connect('127.0.0.1', 'root', '123456', 'DianShiChengJin');
			
			if(!self::$_connectSource){
				die('mysql connect error' . mysqli_error);
			}
			mysqli_set_charset(self::$_connectSource,"utf8");
			mysqli_query(self::$_connectSource, "set names UTF8");
			mysqli_query(self::$_connectSource,"set names ’utf8’ ");  
			mysqli_query(self::$_connectSource,"set character_set_client=utf8");   
			mysqli_query(self::$_connectSource,"set character_set_results=utf8");

		}
		return self::$_connectSource;
	}
}
?>
