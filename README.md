# Excel Module

General Excel Module to implement Excel Download simply and fast.

## Usage - Spring Boot with Rest API 

Excel Module uses annotation and reflections to render excel file.

You need to use @ExcelColumn to Object to be rendered in excel file.

See below example.

### Jitpack

Here, only explain how to manage dependency with gradle.

See other build tool usage from [Jitpack Hompage](https://jitpack.io/) 

```
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}

// Use proper version
dependencies {
  implementation 'com.github.podon0449:excel-download:1.0.2'
}
```

### 1. Poi ExcelColumn 사용법 

```java
// In ExcelDeto
// Rendered Field Order is same as Dto field order
public class ExcelDto {

  // Annotation Case 1. no headerStyle and bodyStyle
  @ExcelColumn(headerName = "User Name")
  private String name;
  
  // Annotation Case 2. use not default style, but use defined style
  @ExcelColumn(headerName = "User Age",
      headerStyle = @ExcelColumnStyle(excelCellStyleClass = DefaultExcelCellStyle.class, enumName = "BLUE_HEADER")
  )
  private int age;
  
  // Annotation Case 3. You can also configure bodyStyle style
  @ExcelColumn(headerName = "Happy BirthDay",
      bodyStyle = @ExcelColumnStyle(excelCellStyleClass = DefaultExcelCellStyle.class, enumName = "BODY")
  )
  private LocalDate birthDay;

}
```

If you want to config default style in class, you should use @DefaultHeaderStyle or @DefaultBodyStyle.
This style will be applied to all fields having not field style in this class.

```java
@DefaultHeaderStyle(
    style = @ExcelColumnStyle(excelCellStyleClass = DefaultExcelCellStyle.class, enumName = "BLUE_HEADER")
)
@DefaultBodyStyle(
	style = @ExcelColumnStyle(excelCellStyleClass = DefaultExcelCellStyle.class, enumName = "BODY")
)
public class ExcelDto {
	
	private String name;
    
    private int age;
    
    private LocalDate birthDay;
	
}
```
### 2. Gc Column 사용법 (PivotTable)
```java
/**
 * 해당 필드와 데이터를 매칭시키는 enum
 * active 를 활성화 시킨경우 pivotFieldOrientation 을 지정할 수 있음.
 *
 *    1 : PivotFieldOrientation.PageField;
 *    2 : PivotFieldOrientation.RowField;
 *    3 : PivotFieldOrientation.DataField;
 *    4 : PivotFieldOrientation.Hidden;
 *    5 : PivotFieldOrientation.ColumnField;
 *
 *    type 은 넘어온 list 인덱스 순서  
 *    PivotEnumModel 은 해당 lib 내장되어 있음 
 * */
public enum UserExcelField implements PivotEnumModel {
    RANK(0,"순위", false, 0),
    USER_IDX(1,"유저정보", true, 2),
    COUNTRY(2,"국가", false, 0),
    DEVICE(3,"기기", false, 0),
    NICKNAME(4,"닉네임" , true, 1),
    EMAIL(5,"이메일" , false, 0),
    AMOUNT(6,"유저 보유금액" , true, 3);

    private int type;
    private String name;
    private boolean active;
    private int pivotFieldOrientation;

    UserExcelFiled(int type, String name, boolean active, int pivotFieldOrientation) {
        this.type = type;
        this.name = name;
        this.active = active;
        this.pivotFieldOrientation = pivotFieldOrientation;
    }
    public String getKey() {
        return name();
    }

    public String getValue() {
        return String.valueOf(this.type);
    }

    public int getType() {
        return this.type;
    }

    public String getName() {
        return this.name;
    }

    public boolean isActive() {
        return active;
    }

    public int getCode() { return this.type;}

    public int getPivotFieldOrientation() {
        return pivotFieldOrientation;
    }


}
```
### 1. Poi lib 사용 예제 

@ExcelColumn DTO 사용 예제 

```java
// In Controller
@RestController
public class Controller {
	
  @GetMapping("/any-url")
  public void poiExcelColumnUse(RequestDto requestDto, HttpServletResponse response) throws IOException {
      // If you specify response type when you use axios,
      // you don't need to set HttpServletResponse contenttype. See #1 in Front with axios section
      response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
      response.setHeader("Content-Disposition", "attachment;filename=excelFile.xlsx");
  
      List<ExcelDto> excelDtos = someService.getRenderedData(requestDto);
      ExcelFile excelFile = new PoiSheetExcelFile(excelDtos, ExcelDto.class);
      excelFile.write(response.getOutputStream());
  }

}
```

유저가 직접 헤더키를 지정하는 경우 사용 예제 
```java
// In Controller
@RestController
public class Controller {
	
  @GetMapping("/any-url")
  public void poiUserColumnUse(RequestDto requestDto, HttpServletResponse response) throws IOException {
      // If you specify response type when you use axios,
      // you don't need to set HttpServletResponse contenttype. See #1 in Front with axios section
      response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
      response.setHeader("Content-Disposition", "attachment;filename=excelFile.xlsx");
      //테스트 데이터 삽입 
      List<Map<String, Object>> excelMetaList = new ArrayList<>();
      for (int i=1; i < 1000; i++) {
          Map<String, Object> metaMap = new HashMap<>();
          metaMap.put("rank", i);
          metaMap.put("userIdx", "1000000" + i);
          metaMap.put("countryName", "한국");
          metaMap.put("device", "Aos");
          metaMap.put("nickname", "나는엑셀테스트");
          metaMap.put("email", "test"+i+"@naver.com");
          metaMap.put("amount", i + 1000);
          excelMetaList.add(metaMap);
      }
      // 해당 헤더 keys
    List<String> headerKeys = Arrays.asList("순위", "유저정보", "국가", "기기", "닉네임", "이메일", "유저 보유 금액");

    List<ExcelDto> excelDtos = someService.getRenderedData(requestDto);
    ExcelFile excelFile = new PoiSheetExcelFile(excelMetaList, headerKeys, USER_COLUMN);
    excelFile.write(response.getOutputStream());
  }

}
```
엑셀 시트 추가 예제 
```java
// In Controller
@RestController
public class Controller {
	
  @GetMapping("/any-url")
  public void addExcelSheet(RequestDto requestDto, HttpServletResponse response) throws IOException {
      // If you specify response type when you use axios,
      // you don't need to set HttpServletResponse contenttype. See #1 in Front with axios section
      response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
      response.setHeader("Content-Disposition", "attachment;filename=excelFile.xlsx");
      List<ExcelDto> excelDtos = someService.getRenderedData(requestDto);
      ExcelFile excelFile = new PoiSheetExcelFile(excelDtos, ExcelDto.class);
      //ExcelColumn 으로 addSheet 하는 경우 
      List<ExcelDetailDto> excelDetailDtos = someService.getRenderedDetailData(requestDto);
      excelFile.addSheet(excelDetailDtos, ExcelDetailDto.class);
      
      //유저가 직접 헤더키를 지정하는 경우  
      List<Map<String, Object>> excelCustomList = new ArrayList<>();
      for (int i=1; i < 1000; i++) {
          Map<String, Object> metaMap = new HashMap<>();
          metaMap.put("rank", i);
          metaMap.put("userIdx", "1000000" + i);
          metaMap.put("countryName", "한국");
          metaMap.put("device", "Aos");
          metaMap.put("nickname", "나는엑셀테스트");
          metaMap.put("email", "test"+i+"@naver.com");
          metaMap.put("amount", i + 1000);
          excelCustomList.add(metaMap);
      }
      List<String> headerKeys = Arrays.asList("순위", "유저정보", "국가", "기기", "닉네임", "이메일", "유저 보유 금액");

      excelFile.addSheet(excelCustomList, headerKeys, USER_COLUMN);
      
      excelFile.write(response.getOutputStream());
  }

}
```

### 2. Gc lib 사용 예제 (PivotTable)
```java
// In Controller
@RestController
public class Controller {
	
  @GetMapping("/any-url")
  public void PivotTable(RequestDto requestDto, HttpServletResponse response) throws IOException {
      response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
      response.setHeader("Content-Disposition", "attachment;filename=excelFile.xlsx");
      
      List<List<Object>> sourceList = new ArrayList<>();

      for (int i=0; i < 100; i++) {
          List<Object> list = new ArrayList<>();
          list.add(i);
          list.add("100000" + i);
          list.add("한국");
          list.add("AOS");
          list.add("nickname_"+ i);
          list.add("erqrk@.naver.com");
          list.add(i + 1000);
          sourceList.add(list);
      }
      ExcelFile excelFile = new GcSheetExcelFile(sourceList, UserExcelFiled.class);

      excelFile.write(response.getOutputStream());
  }

}
```

### Front with axios
```js
axios({
  method: 'GET',
  url: 'server-url',
  responseType: 'blob' // MUST NEED
})
.then(response => {
    // #1 Here, I designate type with response header, however, you can specify 'application/vnd.ms-excel'
    const url
      = window.URL.createObjectURL(new Blobk([response.data], {type : response.headers['content-type']}));
    const link = document.createElement('a');
    link.href = url;
    link.setAttribute('download', 'ExcelFile.xlsx');
    document.body.appendChild(link);
    link.click();
});
```


## Customizing

You can create custom ExcelCellStyle with Class or Enum

If you use Class, you don't need to designate enumName of @ExcelColumnStyle
```java
public class BlueHeaderStyle implements ExcelCellStyle {

  @Override
  public void apply(CellStyle cellStyle) {
    // Do anything you want to change style
    cellStyle.setBlaBla();
  }

} 
``` 

For convenient custom style class, we provide template style class, CustomExcelCellStyle
You can set
- cell color
- 4-side borders type
- cell contents align
Other features will be updated gradually.  

```java
public class BlueHeaderStyle extends CustomExcelCellStyle {

	@Override
	public void configure(ExcelCellStyleConfigurer configurer) {
		configurer.foregroundColor(223, 235, 246)
				.excelBorders(DefaultExcelBorders.newInstance(ExcelBorderStyle.THIN))
				.excelAlign(DefaultExcelAlign.CENTER_CENTER);
	}

}
```

```java
@DefaultHeaderStyle(
    style = @ExcelColumnStyle(excelCellStyleClass = BlueHeaderStyle.class)
)
public class ExcelDto {

  private String field1;

}
```


If you use Enum, you have to specify enumName of @ExcelColumnStyle
```java
public enum CustomCellStyle implements ExcelCellStyle {
	
  CUSTOM_HEADER(field1, field2);
  
  // Whatever fields you need to configure cell style
  private final String field1;
  private final int field2;
  
  @Override
  public void apply(CellStyle cellStyle) {
    // Do anything you want to change style with defined enum fields.
    cellStyle.setBlaBla();
  }
  
}
```  

```java
public class ExcelDto {

  @ExcelColumn(headerName = "Field Header Title",
      bodyStyle = @ExcelColumnStyle(excelCellStyleClass = CustomCellStyle.class, enumName = "CUSTOM_HEADER")
  )
  private String field1;

}
```

## Kinds of Excel File

- PoiSheetExcelFile
- GcSheetExcelFile