package du.db;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface MemberMapper { 
	@Select("select count(*) from members where email = #{email} and password = #{password}")
	public int isMember(MemberDto memberdto);
	
	@Select("select * from members where email = #{email} and password = #{password}")
	public MemberDto findMember(MemberDto memberDto);
	
	@Select("select name from members where email = #{email} and password = #{password}")
	public String findMemberName(MemberDto memberDto);
	
	@Insert("insert into members(member_id, name, email, password) values (members_seq.nextval, #{name}, #{email}, #{password})")
	public void insertMember(MemberDto memberDto);
}
