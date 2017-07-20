package com.changyou.erp.bx.webapp.action;

@Controller
public class BxCommonPageAction extends BaseAction{
	protected final Logger logger = Logger.getLogger(this.getClass());
	
	
    	@RequestMapping(value = "/bx/initSubSubjectTree.do",method={RequestMethod.GET, RequestMethod.POST})
	public void initSubSubjectTree(SubSubjectSearchVo searchvo, HttpServletRequest req, HttpServletResponse res, ModelMap model) throws Exception
	{
		searchvo.setPreNo(req.getParameter("zid"));		
		String id = req.getParameter("id");
		if(!StringUtils.isBlank(id)){
			searchvo.setPreNo(req.getParameter("id"));	
		}		
		List<ZTreeVo> list = bxERPAndOracleManager.getSubSubjectForZtree(searchvo);
		res.getWriter().print(JSONArray.fromObject(list));
	}
	
	@Override
	public List<ZTreeVo> getSubSubjectForZtree(SubSubjectSearchVo postSearchVo) throws Exception {
		List<ZTreeVo> newList = new ArrayList<ZTreeVo>();
		
		List<ApolloWordbookDetail> list = bxERPAndOracleDao.getWordbookDetailByPreNo(postSearchVo.getPreNo());
		for(ApolloWordbookDetail word:list){
			ZTreeVo zTreeVo = new ZTreeVo();
			zTreeVo.setId(word.getSoleNum());
			zTreeVo.setpId(word.getPreNum());
			zTreeVo.setOpen("false");
			zTreeVo.setName(word.getItemName());	
			if(word.getItemRank() == 3){
				zTreeVo.setIsParent("false");
			}else{
				zTreeVo.setIsParent("true");
			}
			newList.add(zTreeVo);
		}
		return newList;
	}
	
	@RequestMapping(value = "/bx/getSubSubjectByTreeNode.do",method={RequestMethod.GET, RequestMethod.POST})
	public void getSubSubjectByTreeNode(@RequestParam("itemId") String itemId, HttpServletRequest request, HttpServletResponse response) throws Exception {
	    Map<String, Object> jsonMap = new HashMap<String, Object>();
	    ApolloWordbookDetail word = bxERPAndOracleManager.getWordbookDetailBySole(itemId);
	    if(word == null){
	    	return;
	    }
        jsonMap.put("soleNum",word.getSoleNum());
        jsonMap.put("itemName",word.getItemName());
        String fieldStringFirst = word.getFieldStringFirst();
        if(word.getFieldStringFirst() == null){
        	fieldStringFirst = "";
        }
        jsonMap.put("fieldStringFirst",fieldStringFirst);
        response.getWriter().print(JsonUtil.mapToJson(jsonMap));
	}
    
}
