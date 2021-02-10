			$(document).ready(function(){
			$("#userTypeError").hide();
			$("#userCodeError").hide();
			$("#userForError").hide();
			$("#userMailError").hide();
			$("#userContactError").hide();
			$("#userIdTypeError").hide();
			//For Default Hide ifOther section
			$("#ifOtherView").hide();

			var userTypeError=false;
			var userCodeError=false;
			var userEmailError=false;
			var	ifOtherError=false;

			$('input[type="radio"][name="userType"]').change(function(){
				validate_userType();
			});

			$("#userCode").keyup(function(){
				$("#userCode").val($("#userCode").val().toUpperCase());
				validate_userCode();
			});
			
			$('input[type="radio"][name="userType"]').change(function(){
				autoFillUserFor();
			});
			
			$("#userEmail").keyup(function(){
				//alert("email");
				validate_userEmail();
			});

			//ifOther text input
			$("#userIdType").change(function(){
				
				var val=$("#userIdType").val();
				if (val=='OTHER') {
					//display if other input
					$("#ifOtherView").show();
				} else {
					$("#ifOtherView").hide();
				}
			});

			//----------------------------------All User Defined functions--------------------------------------------------------------
			function validate_userType(){
				
				var len=$('input[type="radio"][name="userType"]:checked').length;
				if (len==0) {
					$("#userTypeError").show();
					$("#userTypeError").html("Choose One <b>User Type</b>");
					$("#userTypeError").css("color","red");
					userTypeError=false;
				} else {
					$("#userTypeError").hide();
					userTypeError=true;
				}
				return userTypeError;
			}
			//-----------for User code function-------------------------------------------------
			function validate_userCode(){
				var val=$("#userCode").val();
				var exp=/^[A-Z]{2,25}$/;
				if(val==''){
					$("#userCodeError").show();
					$("#userCodeError").html("Enter <b>User Code</b>");
					$("#userCodeError").css("color","red");
					userCodeError=false;
				}else if(!exp.test(val)){
					$("#userCodeError").show();
					$("#userCodeError").html("Enter <b>2-25 chars only</b>");
					$("#userCodeError").css("color","red");
					userCodeError=false;
				}else{
					$("#userCodeError").hide();
					userCodeError = true;
                	//AJ START
                 	$.ajax({
                 		url:'validateusercode',
                 		data:{"code":val},
                 		success:function(resTxt) {
                 			if(resTxt!=""){
                 				 $("#userCodeError").show();
                                  $("#userCodeError").html(resTxt);
                                  $("#userCodeError").css("color", "red");
                                  userCodeError = false;
                 			} else {
                 				 $("#userCodeError").hide();
                                  userCodeError = true;
                 			}
                 		}
                 	}); //AJ-ENd	
				} // else end
				return userCodeError;
			}
			
			function validate_userEmail(){
				//alert("mail11");
				var val=$("#userEmail").val();
				var exp=/^[A-Za-z0-9\.\-]+\@[a-z]+\.[a-z\.]{2,10}$/;
				if(val=='') {
					$("#userEmailError").html("Enter <b>User Mail</b>");
					$("#userEmailError").css("color","red");
					$("#userEmailError").show();
					userEmailError=false;
				} else if(!exp.test(val)){
					$("#userEmailError").html("Enter <b>Valid Mail</b>");
					$("#userEmailError").css("color","red");
					$("#userEmailError").show();
					userEmailError=false;
				}else{
					$.ajax({
						url:'mailcheck',
						data:{'mail':val},
						success:function(resTxt){
							if (resTxt!='') {
								$("#userEmailError").html(resTxt);
								$("#userEmailError").css("color","red");
								$("#userEmailError").show();
								userEmailError=false;
							} else {
								$("#userEmailError").hide();
								userEmailError=true;
							}
						}
					});
				}
				return userEmailError;
			}
			//-----------------------Extra Validations works-----------------=====================
			//-----------------------auto fill for user for---------------------------------------------------
			

			function autoFillUserFor(){
				var val=$('input[type="radio"][name="userType"]:checked').val();
				if (val=='Vendor') {
					$("#userFor").val('Purchase');
				} else if(val=='Customer'){
					$("#userFor").val('Sale');
				}
			}

			///====================IfOther Text Input----------------------------------------------------------------
			$("#userIdType").change(function(){
				
				var val=$("#userIdType").val();
				if(val=='OTHER') {
					$("#ifOther").removeAttr("readOnly");
				} else {
					$("#ifOther").removeAttr("readOnly","true");
					$("#ifOther").val("");
				}
			});

			//---------------------------------On submit---------------------------------------------------------------------------
			$("#whUserTypeForm").submit(function(){
				//alert("whUserTypeForm");
				validate_userType();
				validate_userCode();
				validate_userEmail();
				//validate_userType();

				if(userTypeError && userCodeError && userEmailError ) 
					return true;
				else
					return false;
			});
		});

