package in.codifi.api.helper;

import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import in.codifi.api.entity.AddressEntity;
import in.codifi.api.entity.ApplicationUserEntity;
import in.codifi.api.entity.BankEntity;
import in.codifi.api.entity.IvrEntity;
import in.codifi.api.entity.PaymentEntity;
import in.codifi.api.entity.ProfileEntity;
import in.codifi.api.entity.ResponseCkyc;
import in.codifi.api.repository.AddressRepository;
import in.codifi.api.repository.ApplicationUserRepository;
import in.codifi.api.repository.BankRepository;
import in.codifi.api.repository.CkycResponseRepos;
import in.codifi.api.repository.IvrRepository;
import in.codifi.api.repository.PaymentRepository;
import in.codifi.api.repository.ProfileRepository;
import in.codifi.api.request.model.BankAddressModel;
import in.codifi.api.utilities.CommonMethods;
import in.codifi.api.utilities.StringUtil;

@ApplicationScoped
public class backOfficeHelper {
	
	@Inject
	ApplicationUserRepository applicationUserRepository;
	@Inject
	ProfileRepository profileRepository;
	@Inject
	BankRepository bankRepository;
	@Inject
	CommonMethods commonMethods;
	@Inject
	AddressRepository addressRepository;
	@Inject
	CkycResponseRepos ckycResponseRepos;
	@Inject
	IvrRepository ivrRepository;
	@Inject
	PaymentRepository paymentRepository;
	
	public  String generateXMLContent(long  applicationId ) {
		 String xmlData = "";
	        StringBuffer createDocuBuffer = new StringBuffer();
	        Optional<ApplicationUserEntity> userEntity = applicationUserRepository.findById(applicationId);
	        ProfileEntity getProfile = profileRepository.findByapplicationId(applicationId);
	        BankEntity getBankDetails = bankRepository.findByapplicationId(applicationId);
	        BankAddressModel model = commonMethods.findBankAddressByIfsc(getBankDetails.getIfsc());
	        AddressEntity getAddress = addressRepository.findByapplicationId(applicationId);
	        ResponseCkyc responseCkyc=ckycResponseRepos.findByApplicationId(applicationId);
	        IvrEntity ivr=ivrRepository.findByApplicationId(applicationId);
	        PaymentEntity paymentEntity=paymentRepository.findByApplicationId(applicationId);
	        if(userEntity!=null||getProfile!=null&&getBankDetails!=null||getAddress!=null&&responseCkyc!=null&&ivr!=null&&paymentEntity!=null) {
	        createDocuBuffer.append("<root>");
	        createDocuBuffer.append("<Fileheader>");
	        createDocuBuffer.append("<entityid>eKYC_P9</entityid>");
	        createDocuBuffer.append("<entityname>PhillipCapital (India) Pvt. Ltd</entityname>");
	        createDocuBuffer.append("<filegendttm>1646720944604</filegendttm>");
	        createDocuBuffer.append("<batchno>5</batchno>");
	        createDocuBuffer.append("<totalrecs>5</totalrecs>");
	        createDocuBuffer.append("<instigorefno>5</instigorefno>");
	        createDocuBuffer.append("</Fileheader>");
	        createDocuBuffer.append("<FileDetail>");
	        createDocuBuffer.append("<ClientRecord>");
	        createDocuBuffer.append("<recordheader>");
	        createDocuBuffer.append("<recordtype>01</recordtype>");
	        createDocuBuffer.append("<sritranstnid>5</sritranstnid>");
	        createDocuBuffer.append("<clienttype>I</clienttype>");
	        createDocuBuffer.append("<appdate>2022-03-08</appdate>");
	        createDocuBuffer.append("<apptime>00:00:00.00</apptime>");
	        createDocuBuffer.append("<esign>" + (userEntity.get().getEsignCompleted() == 1 ? "Y" : "N") + "</esign>");
	        createDocuBuffer.append("<dpveridate>2022-03-0800:00:00.00</dpveridate>");
	        createDocuBuffer.append("<branch/>");
	        createDocuBuffer.append("<rm/>");
	        createDocuBuffer.append("<pms/>");
	        createDocuBuffer.append("</recordheader>");
	        createDocuBuffer.append("<firstholder>");
	        createDocuBuffer.append("<clientdetails>");
	        createDocuBuffer.append("<fhpan>"+userEntity.get().getPanNumber()+"</fhpan>");
	        createDocuBuffer.append("<fhpanflag>"+(userEntity.get().getPanConfirm() == 1 ?"Y":"N")+"</fhpanflag>");
	        createDocuBuffer.append("<fhckycno/>");
	        createDocuBuffer.append("<fhprefix>"+responseCkyc.getPrefix()+"</fhprefix>");
	        createDocuBuffer.append("<fhfirstname>"+userEntity.get().getFirstName()+"</fhfirstname>");
	        createDocuBuffer.append("<fhmidname>"+userEntity.get().getMiddleName()+"</fhmidname>");
	        createDocuBuffer.append("<fhlastname>"+userEntity.get().getLastName()+"</fhlastname>");
	        createDocuBuffer.append("<fhfullname>"+userEntity.get().getUserName()+"</fhfullname>");
	        createDocuBuffer.append("<fhresidentialstatus>01</fhresidentialstatus>");
			createDocuBuffer.append(" <fhnationality>01</fhnationality>");
			createDocuBuffer.append("<fhpep>N</fhpep>");
			String occupation = "";
			if (StringUtil.isEqual(getProfile.getOccupation(), "Public Sector")) {
				occupation = "1";
			} else if (StringUtil.isEqual(getProfile.getOccupation(), "Private Sector Service")) {
				occupation = "2";
			} else if (StringUtil.isEqual(getProfile.getOccupation(), "Government Service")) {
				occupation = "3";
			} else if (StringUtil.isEqual(getProfile.getOccupation(), "Business")) {
				occupation = "4";
			} else if (StringUtil.isEqual(getProfile.getOccupation(), "Professional")) {
				occupation = "5";
			} else if (StringUtil.isEqual(getProfile.getOccupation(), "Retired")) {
				occupation = "6";
			} else if (StringUtil.isEqual(getProfile.getOccupation(), "House Wife")) {
				occupation = "7";
			} else if (StringUtil.isEqual(getProfile.getOccupation(), "Student")) {
				occupation = "8";
			} else if (StringUtil.isEqual(getProfile.getOccupation(), "Others")) {
				occupation = "99";
			}
			createDocuBuffer.append("<fhoccupation>"+occupation+"</fhoccupation>");
			createDocuBuffer.append("<fhothtaxresident>01</fhothtaxresident>");
			createDocuBuffer.append("<fhfatcadeclaration>Y</fhfatcadeclaration>");
			createDocuBuffer.append("<fhpastregulatryactn>N</fhpastregulatryactn>");
			createDocuBuffer.append("<fheducatnquali>Not Provided</fheducatnquali>");
			createDocuBuffer.append(" <fhbirthcountry>India</fhbirthcountry>");
			createDocuBuffer.append("<fhsignature>0x49492A00B8000000F9955F9DBE44D1077389061327D922154FE46A08F134A16C12F552EB74D2C4D130FE6793D093F5343CBAC275E932E69D7A68D10B0F3682408BFFD020420810950D6D0FD1EEC8BD4D6BFF509DE3BAFD9DD7EABDF17AB0CA7045F9A10D91FF492DFD7F9E5F88B0EBB60DB4BD7</fhsignature>");
			if(model!=null) {
			createDocuBuffer.append("<fhapplnamebank>"+model.getBank()+"</fhapplnamebank>");
			createDocuBuffer.append(" <fhapplnameitd>"+getProfile.getApplicantName()+"</fhapplnameitd>");
			createDocuBuffer.append("<fhapplnamekyc>"+getProfile.getApplicantName()+"</fhapplnamekyc>");
			createDocuBuffer.append("<fhkycmode></fhkycmode>");
			createDocuBuffer.append("<fhkycdate></fhkycdate>");
	        createDocuBuffer.append("</clientdetails>");
	        createDocuBuffer.append("<clientcontactdetails>");
	        createDocuBuffer.append("<fhmobilenumber>"+userEntity.get().getMobileNo()+"</fhmobilenumber>");
	        createDocuBuffer.append("<fhfamilyflagmobile>Y</fhfamilyflagmobile>");
	        createDocuBuffer.append("<fhemailid>"+userEntity.get().getEmailId()+"</fhemailid>");
	        createDocuBuffer.append("<fhfamilyflagemailid>Y</fhfamilyflagemailid>");
	        String PerAddress1=(getAddress.getIsKra()==1?getAddress.getKraPerAddress1():getAddress.getAddress1());
			String PerAddress2=(getAddress.getIsKra()==1?getAddress.getKraPerAddress2():getAddress.getAddress2());
			String PerAddress3=(getAddress.getIsKra()==1?getAddress.getKraPerAddress3():getAddress.getAddress2());
			String State=(getAddress.getIsKra()==1?getAddress.getKraPerState():getAddress.getState());
			String City=(getAddress.getIsKra()==1?getAddress.getKraPerCity():getAddress.getLandmark());
			String dist=(getAddress.getIsKra()==1?getAddress.getKraPerCity():getAddress.getDistrict());
			String Pincode=(getAddress.getIsKra()==1?Integer.toString(getAddress.getKraPerPin()):getAddress.getPincode().toString());
			String CurAddress1=(getAddress.getIsKra()==1?responseCkyc.getCorresLine1():responseCkyc.getPermLine1());
			String CurAddress2=(getAddress.getIsKra()==1?responseCkyc.getCorresLine2():responseCkyc.getPermLine2());
			String CurAddress3=(getAddress.getIsKra()==1?responseCkyc.getCorresLine3():responseCkyc.getPermLine3());
			String CurState=(getAddress.getIsKra()==1?responseCkyc.getCorresState():responseCkyc.getPermState());
			String CurCity=(getAddress.getIsKra()==1?responseCkyc.getCorresDist():responseCkyc.getPermDist());
			String Curdist=(getAddress.getIsKra()==1?responseCkyc.getCorresDist():responseCkyc.getPermDist());
			String CurPincode=(getAddress.getIsKra()==1?responseCkyc.getCorresPin():responseCkyc.getPermPin());
	        createDocuBuffer.append("<fhcorraddress>");
	        createDocuBuffer.append("<fhcorraddline1> "+CurAddress1+"</fhcorraddline1>");
			createDocuBuffer.append("<fhcorraddline2>"+CurAddress2+"</fhcorraddline2>");
			createDocuBuffer.append("<fhcorraddline3>"+CurAddress3+"</fhcorraddline3>");
			createDocuBuffer.append("<fhcorraddcity>"+CurCity+"</fhcorraddcity>");
			createDocuBuffer.append(" <fhcorraddpincode>"+CurPincode+"</fhcorraddpincode>");
			createDocuBuffer.append("<fhcorradddistrict>"+Curdist+"</fhcorradddistrict>");
			createDocuBuffer.append(" <fhcorraddstate>"+CurState+"</fhcorraddstate>");
			createDocuBuffer.append("<fhcorraddcountry>IN</fhcorraddcountry>");
			createDocuBuffer.append("<fhcorraddprooftype>26</fhcorraddprooftype>");
	        createDocuBuffer.append("<fhcorraddidvaliddate/>");
			createDocuBuffer.append("<fhcorraddtype>01</fhcorraddtype>");
			createDocuBuffer.append("</fhcorraddress>");
			createDocuBuffer.append("<fhcorrsameaspermanent>Y</fhcorrsameaspermanent>");
			createDocuBuffer.append("<fhpermaddress>");
			 createDocuBuffer.append("<fhpermaddline1>"+PerAddress1+"</fhpermaddline1>");
	         createDocuBuffer.append("<fhpermaddline2>"+PerAddress2+"</fhpermaddline2>");
	         createDocuBuffer.append("<fhpermaddline3>"+PerAddress3+"</fhpermaddline3>");
	         createDocuBuffer.append("<fhpermaddcity>"+City+"</fhpermaddcity>");
	         createDocuBuffer.append("<fhpermaddpincode>"+Pincode+"</fhpermaddpincode>");
	         createDocuBuffer.append("<fhpermadddistrict>"+dist+"</fhpermadddistrict>");
	         createDocuBuffer.append("<fhpermaddstate>"+State+"</fhpermaddstate>");
	         createDocuBuffer.append("<fhpermaddcountry/>");
	         createDocuBuffer.append("<fhpermaddprooftype/>");
	         createDocuBuffer.append("<fhpermaddidnumber/>");
	         createDocuBuffer.append("<fhpermaddidvaliddate/>");
	         createDocuBuffer.append("<fhpermaddtype/>");
	         createDocuBuffer.append("</fhpermaddress>");
	         createDocuBuffer.append("</clientcontactdetails>");
	         createDocuBuffer.append("<ipv>");
			 createDocuBuffer.append("<fhipvemplname/>");
			 createDocuBuffer.append("<fhipvempldesig/>");
			 createDocuBuffer.append("<fhipvemplbranch/>");
			 createDocuBuffer.append("<fhipvemplcode/>");
			 createDocuBuffer.append("<fhipvorganiname/>");
			 createDocuBuffer.append("<firsipvorganicode/>");
			 createDocuBuffer.append("</ipv>");
			 createDocuBuffer.append("<fhlatitude>"+ivr.getLatitude()+"</fhlatitude>");
			 createDocuBuffer.append("<fhlongitude>"+ivr.getLatitude()+"</fhlongitude>");
			 createDocuBuffer.append("<fhplacelatlong/>");
			createDocuBuffer.append("</firstholder>");
			createDocuBuffer.append("<financial>");
			
			String annual_income = "";
			if (StringUtil.isEqual(getProfile.getAnnualIncome(), "Below 1 lakh")) {
				annual_income = "1";
			} else if (StringUtil.isEqual(getProfile.getAnnualIncome(), "1-5 lakhs")) {
				annual_income = "2";
			} else if (StringUtil.isEqual(getProfile.getAnnualIncome(), "5-10 lakhs")) {
				annual_income = "3";
			} else if (StringUtil.isEqual(getProfile.getAnnualIncome(), "10-25 lakhs")) {
				annual_income = "4";
			} else if (StringUtil.isEqual(getProfile.getAnnualIncome(), "> 25 lakhs")) {
				annual_income = "5";
			}
			 createDocuBuffer.append("<incomornetowrth>1</incomornetowrth>");
	         createDocuBuffer.append("<annualincomrang>"+annual_income+"</annualincomrang>");
	         createDocuBuffer.append("<dateannualincome>07-03-202200:00:00.00</dateannualincome>");
	         createDocuBuffer.append("<networth>"+getProfile.getNetWorth()+"</networth>");
	         createDocuBuffer.append("<networthdate>"+getProfile.getNetWorthDate()+"</networthdate>");
	         createDocuBuffer.append("<bankdetails>");
	         createDocuBuffer.append("<bankaccountno>"+getBankDetails.getAccountNo()+"</bankaccountno>");
	         createDocuBuffer.append("<bankname>"+model.getBank()+"</bankname>");
	         createDocuBuffer.append("<bankaccounttype>01</bankaccounttype>");
	         createDocuBuffer.append("<branchcode>"+model.getBankcode()+"</branchcode>");
	        createDocuBuffer.append("<ifsc>"+getBankDetails.getIfsc()+"</ifsc>");
	        createDocuBuffer.append("<micr>"+getBankDetails.getMicr()+"</micr>");
	        createDocuBuffer.append("<bankaddress>"+getBankDetails.getAddress()+"</bankaddress>");
			createDocuBuffer.append("</bankdetails>");
			createDocuBuffer.append("<incomesource>self</incomesource>");
			createDocuBuffer.append("</financial>");
			createDocuBuffer.append("<accountdetails>");
		    createDocuBuffer.append("<modecontractnote>Electronic</modecontractnote>");
		    createDocuBuffer.append("<runningaccountfreq>Quarterly</runningaccountfreq>");
		    createDocuBuffer.append("<tradingeperience>"+(getProfile.getTradingExperience()=="Nil"?"N":"Y")+"</tradingeperience>");
		    createDocuBuffer.append("<brokerageoption>Basic</brokerageoption>");
		    createDocuBuffer.append("<internettrading>Y</internettrading>");
		    createDocuBuffer.append("<introducer>N</introducer>");
		    createDocuBuffer.append("<paymentscheme>NA</paymentscheme>");
		    createDocuBuffer.append("<tariffplan>NA</tariffplan>");
		    createDocuBuffer.append("<suborotherborker>N</suborotherborker>");
		    createDocuBuffer.append("<accountstatus>Y</accountstatus>");
		    createDocuBuffer.append("<poafund>N</poafund>");
		    createDocuBuffer.append("<poasecurities>N</poasecurities>");
		    createDocuBuffer.append("<poafunddate/>");
		    createDocuBuffer.append("<poasecuritiesdate/>");
		    createDocuBuffer.append("<nsetrading>");
	        createDocuBuffer.append("<nseucc>G900000005</nseucc>");
	        createDocuBuffer.append("<nsecash>Y</nsecash>");
	        createDocuBuffer.append("<nsefo>Y</nsefo>");
	        createDocuBuffer.append("<nsecurrency>Y</nsecurrency>");
	        createDocuBuffer.append("<nsecommodity>N</nsecommodity>");
	        createDocuBuffer.append("<nsemfss>N</nsemfss>");
	        createDocuBuffer.append("<nseclientcategory>I</nseclientcategory>");
	        createDocuBuffer.append("<nseclientstatus>A</nseclientstatus>");
			createDocuBuffer.append("</nsetrading>");
			createDocuBuffer.append("<bsetrading>");
	        createDocuBuffer.append("<bseucc>G900000005</bseucc>");
	        createDocuBuffer.append("<bsecash>Y</bsecash>");
	        createDocuBuffer.append("<bsefo>Y</bsefo>");
	        createDocuBuffer.append("<bsecurrency>Y</bsecurrency>");
	        createDocuBuffer.append("<bsecommodity>N</bsecommodity>");
	        createDocuBuffer.append("<bsestarmf>N</bsestarmf>");
	        createDocuBuffer.append("<bseclientcategory>I</bseclientcategory>");
	        createDocuBuffer.append("<bseclientstatus>CL</bseclientstatus>");
	        createDocuBuffer.append("<bsesmsemailalert>Y</bsesmsemailalert>");
	        createDocuBuffer.append("</bsetrading>");
	        createDocuBuffer.append("<mcxtrading>");
	        createDocuBuffer.append("<mcxclientcategory>02</mcxclientcategory>");
	        createDocuBuffer.append("<mcxclientstatus>1</mcxclientstatus>");
	        createDocuBuffer.append("<mcxclienttype>1</mcxclienttype>");
	        createDocuBuffer.append("</mcxtrading>");
	        createDocuBuffer.append("<nsdldemat>");
	        createDocuBuffer.append("<nsdlclienttype>1</nsdlclienttype>");
	        createDocuBuffer.append("<nsdlclientsubtype>01</nsdlclientsubtype>");
	        createDocuBuffer.append("<nsdldpid>IN302164</nsdldpid>");
	        createDocuBuffer.append("<nsdldpname>PhillipCapital (India) Pvt. Ltd</nsdldpname>");
	        createDocuBuffer.append("<dematclientid>60000078</dematclientid>");
	        createDocuBuffer.append("<nonomination/>");
	        createDocuBuffer.append("<nonbsda>N</nonbsda>");
	        createDocuBuffer.append("<siforcredit>Y</siforcredit>");
	        createDocuBuffer.append("<estatement/>");
	        createDocuBuffer.append("<eCommIssuer/>");
	        createDocuBuffer.append("<smsalert>Y</smsalert>");
	        createDocuBuffer.append("<disbookletfacility>N</disbookletfacility>");
	        createDocuBuffer.append("<moderightsobligatn>Electronic</moderightsobligatn>");
	        createDocuBuffer.append("<poaoperated>N</poaoperated>");
	        createDocuBuffer.append("</nsdldemat>");
	        createDocuBuffer.append("<cdsldemat>");
	        createDocuBuffer.append("<cdslclienttype/>");
	        createDocuBuffer.append("<cdslclientsubtype/>");
	        createDocuBuffer.append("<dematclientid/>");
	        createDocuBuffer.append("<cdsldpname/>");
	        createDocuBuffer.append("<nonomination/>");
	        createDocuBuffer.append("<nonbsda/>");
	        createDocuBuffer.append("<siforcredit/>");
	        createDocuBuffer.append("<estatement/>");
	        createDocuBuffer.append("<eCommIssuer/>");
	        createDocuBuffer.append("<smsalert/>");
	        createDocuBuffer.append("<disbookletfacility/>");
	        createDocuBuffer.append("<moderightsobligatn/>");
	        createDocuBuffer.append("<poaoperated/>");
	        createDocuBuffer.append("</cdsldemat>");
	        createDocuBuffer.append("</accountdetails>");
	        createDocuBuffer.append("<document>");
		    createDocuBuffer.append("<fhpancopy></fhpancopy>");
		    createDocuBuffer.append("<fhsignature></fhsignature>");
		    createDocuBuffer.append("<fhphoto></fhphoto>");
		    createDocuBuffer.append("<cheque/>");
		    createDocuBuffer.append("<cancelcheque></cancelcheque>");
		    createDocuBuffer.append("<fhprofcorradd/>");
		    createDocuBuffer.append("<fhprofpermadd/>");
		    createDocuBuffer.append("<bankstmnt></bankstmnt>");
		    createDocuBuffer.append("<fhaadharxml></fhaadharxml>");
		    createDocuBuffer.append("<nomineform/>");
		    createDocuBuffer.append("<otherdoc></otherdoc>");
		    createDocuBuffer.append("</document>");
		    createDocuBuffer.append("<paymentdetails>");
		    createDocuBuffer.append("<pgrefno></pgrefno>");
		    createDocuBuffer.append("<amountpaid>"+paymentEntity.getAmountPaid()+"</amountpaid>");
		    createDocuBuffer.append("<dateofpayment></dateofpayment>");
			createDocuBuffer.append("</paymentdetails>");
			createDocuBuffer.append("</ClientRecord>");
			createDocuBuffer.append("</FileDetail>");
			createDocuBuffer.append("</root>");
			xmlData = createDocuBuffer.toString();
			}	
	 }
			return xmlData;}}
