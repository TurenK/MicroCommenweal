<?php
header('Content-type: application/json;charset=utf-8');
require_once('./response.php');
 
if(empty($_FILES)) 
	return Response::show(407,"false","no files received");
 
$dirPath = './img/';//�����ļ������Ŀ¼
 
if(!is_dir($dirPath)){
  //Ŀ¼�������򴴽�Ŀ¼
  @mkdir($dirPath);
}
 
$count = count($_FILES);//�����ļ���

$success = $failure = 0;
 
if($count !== 1) 
	return Response::show(408,"false","more than one received");
 
foreach($_FILES as $key => $value){
  //ѭ����������
  $tmp = $value['name'];//��ȡ�ϴ��ļ���
  $tmpName = $value['tmp_name'];//��ʱ�ļ�·��
  //�ϴ����ļ��ᱻ���浽php��ʱĿ¼�����ú������ļ����Ƶ�ָ��Ŀ¼
  $realpath = $dirPath.date('YmdHis').'_'.$tmp;
  if(move_uploaded_file($tmpName,$realpath)){
    $success++;
  }else{
    $failure++;
  }
}

if($success > 0){
	return Response::show(200,"true", $realpath);
}else{
	return Response::show(400,"false","data deliver fail");
}
?>