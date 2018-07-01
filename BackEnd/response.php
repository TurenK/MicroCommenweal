<?php
header("Content-type:text/html;charset=utf8");
class Response{
	/**
	*按json方式输出通信信息
	*@param integer $code 状态码
	*@param String $message 提示信息
	*@param array $data 数据
	*return string
	*/
	public static function show($code, $message = '',$data = array()){
		if(!is_numeric($code)){
			return '';
		}

		$result = array(
		'code' => $code,
		'message' => $message,
		'data' => $data);
		
		ob_clean();
		echo trim(str_replace(array('[',']'),null,json_encode($result)));
		//echo json_encode($result,JSON_UNESCAPED_UNICODE);
		exit;
	}
}
?>