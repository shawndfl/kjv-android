select bookid, chapter, count(*) as count from booklogs
group by bookid, chapter having count(*) > 1 order by bookid