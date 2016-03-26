--select bookid from booklogs a, books b where a.bookid = b._id group by a.bookid having count(distinct a.chapter) == b.chapterCount
--select a.bookid, b.chaptercount, a.chapter  from booklogs a, books b where a.bookid = b._id group by a.bookid, a.chapter
--select bookid from readstate a, books b where a.bookid = b._id group by a.bookid having count(distinct a.chapter) == b.chapterCount
--select * from booklogs where bookid =0 order by chapter
select b._id id, 
b.name name, 
strftime('%m-%d-%Y', a.timestamp / 1000, 'unixepoch') date, 
count(distinct a.chapter) total, 
count(distinct c.chapter) >0 hasReadState, 
Round(((1.0*count(distinct a.chapter))  / (1.0*b.chapterCount)) * 100.0) percent 
	from booklogs a 
	inner join books b on b._id = a.bookid
	left join readstate c  on c.bookid = a.bookid 
	group by a.bookid 
	having max(a.timestamp) 
union 
 select _id id,  name, '' , '0' , '0' , '0' from books  except 
  select  bookid, name, '', '0',  '0' , '0' from booklogs, books where books._id = bookid
 order by id