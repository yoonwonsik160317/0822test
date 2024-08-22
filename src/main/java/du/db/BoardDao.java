package du.db;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import du.db.BoardDto;

import mybatis.SqlMapConfig;

public class BoardDao {

	// SqlSessionFactory를 SqlMapConfig를 통하여 생성한다.
	SqlSessionFactory sqlsession_f = SqlMapConfig.getSqlMapInstance();
	SqlSession session;

	public BoardDao() {
		// SqlSessionFactory에서 session을 할당받는다.
		// 이 때 openSession에 true를 주어야 자동 커밋이 된다.
		// default는 false이다.
		session = sqlsession_f.openSession(true);
	}

	public int getNumRecords() {
		return session.selectOne("BoardMapper.getNumRecords");
	}

	public List<BoardDto> selectList(int start, int listSize) {
		// session을 통해 쿼리를 실행하고 값을 받아온다.
		Map<String, Object> map = new HashMap<>();
		map.put("start", start);
		map.put("listSize", start + listSize - 1);
		return session.selectList("BoardMapper.selectList", map);
	}

	private void updateHits(int num) {
		session.update("BoardMapper.updateHits", num);
	}

	public BoardDto selectOne(int num, boolean hitsIncreased) {
		if (hitsIncreased) {
			updateHits(num);
		}
		return session.selectOne("BoardMapper.selectOne", num);
	}

	public void insertOne(BoardDto dto) {
		session.insert("BoardMapper.insertBoard", dto);
	}


	public void updateOne(BoardDto dto) {
		session.update("BoardMapper.updateBoard", dto);
	}

	public boolean deleteOne(int num) {
		try {
			session.update("BoardMapper.deleteBoard", num);
		} catch(Exception e) {
			System.out.println("여기!!!");
			//e.printStackTrace();
			return true;
		}
		return false;
		
	}

}