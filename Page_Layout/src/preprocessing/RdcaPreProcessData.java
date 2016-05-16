package preprocessing;


//@Entity
//@Table(name="rdca_pre_process_data")
public class RdcaPreProcessData {	
	//@Id
	//@GeneratedValue(strategy=GenerationType.AUTO)
	
	private Long requestid;

	private Long actionid;
	
	private Long id;
	
	//@Column(length=100)
	private String originalFileLocation;
	
	//@Column (length=100)
	private String croppedFileLocation;
	
	private Integer physicalPageNo;
	
	private Boolean bilingual;
	
	private Integer leftPostion;
	
	private Integer middlePostion;
	
	private Integer rightPosition;

	private Boolean pageHasImage=false;
	
	private Boolean englishTextExtract=false;
	
	private String englishFileLocation;
	
	private String englishCharFileLocation;
	
	private Boolean arabicTextExtract=false;
	
	private String arabicFileLocation;
	
	private String arabicCharFileLocation;
	
	private String pageNo;
	
	private String revision;
	
	

	public Long getRequestid()
	{
		return requestid;
	}

	public void setRequestid(Long requestid)
	{
		this.requestid = requestid;
	}

	public Long getActionid()
	{
		return actionid;
	}

	public void setActionid(Long actionid)
	{
		this.actionid = actionid;
	}

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public String getOriginalFileLocation()
	{
		return originalFileLocation;
	}

	public void setOriginalFileLocation(String originalFileLocation)
	{
		this.originalFileLocation = originalFileLocation;
	}

	public String getCroppedFileLocation()
	{
		return croppedFileLocation;
	}

	public void setCroppedFileLocation(String croppedFileLocation)
	{
		this.croppedFileLocation = croppedFileLocation;
	}

	public Integer getPhysicalPageNo()
	{
		return physicalPageNo;
	}

	public void setPhysicalPageNo(Integer physicalPageNo)
	{
		this.physicalPageNo = physicalPageNo;
	}

	public Boolean getBilingual()
	{
		return bilingual;
	}

	public void setBilingual(Boolean bilingual)
	{
		this.bilingual = bilingual;
	}

	public Integer getLeftPostion()
	{
		return leftPostion;
	}

	public void setLeftPostion(Integer leftPostion)
	{
		this.leftPostion = leftPostion;
	}

	public Integer getMiddlePostion()
	{
		return middlePostion;
	}

	public void setMiddlePostion(Integer middlePostion)
	{
		this.middlePostion = middlePostion;
	}

	public Integer getRightPosition()
	{
		return rightPosition;
	}

	public void setRightPosition(Integer rightPosition)
	{
		this.rightPosition = rightPosition;
	}

	public Boolean getPageHasImage()
	{
		return pageHasImage;
	}

	public void setPageHasImage(Boolean pageHasImage)
	{
		this.pageHasImage = pageHasImage;
	}

	public Boolean getEnglishTextExtract()
	{
		return englishTextExtract;
	}

	public void setEnglishTextExtract(Boolean englishTextExtract)
	{
		this.englishTextExtract = englishTextExtract;
	}

	public String getEnglishFileLocation()
	{
		return englishFileLocation;
	}

	public void setEnglishFileLocation(String englishFileLocation)
	{
		this.englishFileLocation = englishFileLocation;
	}

	public String getEnglishCharFileLocation()
	{
		return englishCharFileLocation;
	}

	public void setEnglishCharFileLocation(String englishCharFileLocation)
	{
		this.englishCharFileLocation = englishCharFileLocation;
	}

	public Boolean getArabicTextExtract()
	{
		return arabicTextExtract;
	}

	public void setArabicTextExtract(Boolean arabicTextExtract)
	{
		this.arabicTextExtract = arabicTextExtract;
	}

	public String getArabicFileLocation()
	{
		return arabicFileLocation;
	}

	public void setArabicFileLocation(String arabicFileLocation)
	{
		this.arabicFileLocation = arabicFileLocation;
	}

	public String getArabicCharFileLocation()
	{
		return arabicCharFileLocation;
	}

	public void setArabicCharFileLocation(String arabicCharFileLocation)
	{
		this.arabicCharFileLocation = arabicCharFileLocation;
	}

	public String getPageNo()
	{
		return pageNo;
	}

	public void setPageNo(String pageNo)
	{
		this.pageNo = pageNo;
	}

	public String getRevision()
	{
		return revision;
	}

	public void setRevision(String revision)
	{
		this.revision = revision;
	}

	public RdcaPreProcessData() {
		super();
		// TODO Auto-generated constructor stub
	}

	public RdcaPreProcessData(Long id, String originalFileLocation,
			String croppedFileLocation, Integer pageNo, Boolean bilingual,
			Integer leftPostion, Integer middlePostion, Integer rightPosition) {
		super();
		this.id = id;
		this.originalFileLocation = originalFileLocation;
		this.croppedFileLocation = croppedFileLocation;
		this.physicalPageNo = pageNo;
		this.bilingual = bilingual;
		this.leftPostion = leftPostion;
		this.middlePostion = middlePostion;
		this.rightPosition = rightPosition;
	}

	public RdcaPreProcessData(String originalFileLocation,
			String croppedFileLocation, Integer pageNo, Boolean bilingual,
			Integer leftPostion, Integer middlePostion, Integer rightPosition) {
		super();
		this.originalFileLocation = originalFileLocation;
		this.croppedFileLocation = croppedFileLocation;
		this.physicalPageNo = pageNo;
		this.bilingual = bilingual;
		this.leftPostion = leftPostion;
		this.middlePostion = middlePostion;
		this.rightPosition = rightPosition;
	}

	@Override
	public String toString() {
		return "RdcaPreProcessData [id=" + id + ", originalFileLocation="
				+ originalFileLocation + ", croppedFileLocation="
				+ croppedFileLocation + ", pageNo=" + physicalPageNo + ", bilingual="
				+ bilingual + ", leftPostion=" + leftPostion
				+ ", middlePostion=" + middlePostion + ", rightPosition="
				+ rightPosition + "]";
	}
	
	
}
