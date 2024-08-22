package du.db;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface BoardMapper {
	
	@Select("select * from board")
	public List<BoardDto> findAll(); 
	

}
