<?php
require_once('DB_Connect.php');
 $dbobj = new dbconnect();
 $con = $dbobj->connect();
 
if($con == mysqli_connect_error())
	 echo "Error in connecting with the database ";
 else{
	    $UserID = $_POST['userID'];
	    $website = $_POST['website'];
	    $password = $_POST['password'];
        $passPhrase = $_POST['template'];
        $tempID = $_POST['tempID']; 

	 // check to see if the passphrase exists or not
	 $sel_temp_ans = "select TemplateAnswer from template_answers where TemplateAnswer = '$passPhrase'";

	 if($result_sel_temp_ans = mysqli_query($con,$sel_temp_ans)){
		 while($row_value = mysqli_fetch_assoc($result_sel_temp_ans)){
			 $tmpAnswer = $row_value['TemplateAnswer'];
		 }
	 }
     
	 $isError = false;
	  if($tmpAnswer == ''){
			//----------generate template end----------
		$ins_temp_ans = "INSERT INTO template_answers (TemplateAnswer,tempID) VALUES ('$passPhrase',$tempID)";
		$results_temp_ans = mysqli_query($con,$ins_temp_ans);
		//	----------save template in table end----------
		if(!(mysqli_affected_rows($con) > 0)){
			echo "Issue with saving temp answer";
			$isError = true;
		}
	  }	 
	if(!$isError) {
		$encodepwd = base64_encode($password);
		$query = "insert into masterpasswords (encryPasswd) values ('$encodepwd')";
		$result = mysqli_query($con,$query);
		if(mysqli_affected_rows($con) <= 0 )
			echo mysqli_error($con); 
		else {
		$query = "select TempAID from template_answers where TemplateAnswer = '$passPhrase'";
		$resultvalue = mysqli_query($con,$query);
		$row_value = mysqli_fetch_assoc($resultvalue);
		$tempAID = $row_value["TempAID"];
		
		$query = "select PwdID from masterpasswords where encryPasswd = '$encodepwd'";
		$resultvalue = mysqli_query($con,$query);
		$row_value = mysqli_fetch_assoc($resultvalue);
		$PwdID = $row_value["PwdID"];
		
		$query = "insert into userpasswords (UID,PwdID,tempAID,website) values ($UserID,$PwdID,$tempAID,'$website')";
		$result = mysqli_query($con,$query);
		if(mysqli_affected_rows($con) > 0 )
			echo "Saved Mapping to User Details <br>";
		else
			echo mysqli_error($con);
                }
	}
 }
?>					