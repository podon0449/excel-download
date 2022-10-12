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
  implementation 'com.github.podon0449:excel-download:1.0.0'
}
```

### Server

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


@ExcelColumn DTO 사용 예제 

```java
// In Controller
@RestController
public class Controller {
	
  @GetMapping("/any-url")
  public void poiExcelColumnUse(RequestDto requestDto, HttpServletResponse response) throws IOException {
      // If you specify response type when you use axios,
      // you don't need to set HttpServletResponse contenttype. See #1 in Front with axios section
      response.setContentType("application/vnd.ms-excel");
  
      List<ExcelDto> excelDtos = someService.getRenderedData(requestDto);
      ExcelFile excelFile = new PoiSheetExcelFile(excelDtos, ExcelDto.class);
      excelFile.write(response.getOutputStream());
  }

}
```

Key custom 사용 예제 
```java
// In Controller
@RestController
public class Controller {
	
  @GetMapping("/any-url")
  public void poiUserColumnUse(RequestDto requestDto, HttpServletResponse response) throws IOException {
      // If you specify response type when you use axios,
      // you don't need to set HttpServletResponse contenttype. See #1 in Front with axios section
      response.setContentType("application/vnd.ms-excel");
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
      String[] keys = {"순위", "유저정보", "국가", "기기", "닉네임", "이메일", "유저 보유 금액"};
      
    List<ExcelDto> excelDtos = someService.getRenderedData(requestDto);
    ExcelFile excelFile = new PoiSheetExcelFile<>(excelDtos, ExcelDto.class);
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

- OneSheetExcelFile
- MultiSheetExcelFile