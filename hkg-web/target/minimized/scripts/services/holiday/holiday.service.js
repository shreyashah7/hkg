define(["hkg"],function(a){a.register.factory("ManageHolidayService",["$resource","$rootScope",function(d,c){var b=d(c.apipath+"holiday/:action",{},{retrieveAllHoliday:{method:"GET",isArray:false,params:{action:"retrieve"}},retrieveHolidayById:{method:"POST",isArray:false,params:{action:"retrieve"}},retrievePreviousYearDistinctHoliday:{method:"POST",isArray:false,params:{action:"retrievePreviousYearDistinctHoliday"}},saveHoliday:{method:"PUT",isArray:false,params:{action:"create"}},editHoliday:{method:"POST",isArray:false,params:{action:"update"}},removeHoliday:{method:"POST",params:{action:"delete"}},forceHolidayAdd:{method:"POST",params:{action:"forceHolidayAdd"}}});
return b
}])
});