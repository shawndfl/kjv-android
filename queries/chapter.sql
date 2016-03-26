--select bookid from booklogs a, books b where a.bookid = b._id group by a.bookid having count(distinct a.chapter) == b.chapterCount
--select a.bookid, b.chaptercount, a.chapter  from booklogs a, books b where a.bookid = b._id group by a.bookid, a.chapter
--select bookid from readstate a, books b where a.bookid = b._id group by a.bookid having count(distinct a.chapter) == b.chapterCount
--select * from booklogs where bookid =0 order by chapter
select b.chapterCount, a.bookid, a.chapter, strftime('%m-%d-%Y', a.timestamp / 1000, 'unixepoch') date
 from booklogs a
	join books b on b._id = a.bookid 
	where a.bookid = 0
	group by a.bookid, a.chapter 
	having max(a.timestamp) 